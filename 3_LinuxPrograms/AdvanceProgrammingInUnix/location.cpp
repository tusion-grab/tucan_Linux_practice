#include "stdafx.h"
#include <windows.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <assert.h>
#include "location.h"

//This is the Main entrance
using namespace std;

int APIENTRY _tWinMain(HINSTANCE hInstance,HINSTANCE hPrevInstance,LPTSTR lpCmdLine,int nCmdShow)
{
	UNREFERENCED_PARAMETER(hPrevInstance);
	UNREFERENCED_PARAMETER(lpCmdLine);
	//TODO: Place code here.
	MSG msg;
	HACCEL hAccelTable;
	//Initialize global strings
	LoadString(hInstance, IDS_APP_TITLE, szTitle, MAX_LOADSTRING);
	LoadString(hInstance, IDC_LOCATION, szWindowClass, MAX_LOADSTRING);
	MyRegisterClass(hInstance);
	//initialize the critical section
    InitializeCriticalSection(&m_SensorDataLock);
	//sensor initialization,variable and parameter initial
	Sensor_Initialization();
	//Perform application initialization by default:
	if(!InitInstance (hInstance, nCmdShow))
	{
		return FALSE;
	}
    hAccelTable = LoadAccelerators(hInstance, MAKEINTRESOURCE(IDC_LOCATION));
   //-----------------------------------------------------------------------------------------------------------
   //Main message loop by default:
    while (GetMessage(&msg, NULL, 0, 0))
	{
		if (!TranslateAccelerator(msg.hwnd, hAccelTable, &msg))
		{
			TranslateMessage(&msg);
			DispatchMessage(&msg);
		}
	}
	DeleteCriticalSection(&m_SensorDataLock);
	mncapi_close(handle);
	return (int) msg.wParam;
}

//
//  FUNCTION: MyRegisterClass() by default
//
//  PURPOSE: Registers the window class.
//
//  COMMENTS:
//
//    This function and its usage are only necessary if you want this code
//    to be compatible with Win32 systems prior to the 'RegisterClassEx'
//    function that was added to Windows 95. It is important to call this function
//    so that the application will get 'well formed' small icons associated
//    with it.
//
ATOM MyRegisterClass(HINSTANCE hInstance)
{
	WNDCLASSEX wcex;

	wcex.cbSize = sizeof(WNDCLASSEX);

	wcex.style			= CS_HREDRAW | CS_VREDRAW;
	wcex.lpfnWndProc	= WndProc;
	wcex.cbClsExtra		= 0;
	wcex.cbWndExtra		= 0;
	wcex.hInstance		= hInstance;
	wcex.hIcon			= LoadIcon(hInstance, MAKEINTRESOURCE(IDI_LOCATION));
	wcex.hCursor		= LoadCursor(NULL, IDC_ARROW);
	wcex.hbrBackground	= (HBRUSH)(COLOR_WINDOW+1);
	wcex.lpszMenuName	= MAKEINTRESOURCE(IDC_LOCATION);
	wcex.lpszClassName	= szWindowClass;
	wcex.hIconSm		= LoadIcon(wcex.hInstance, MAKEINTRESOURCE(IDI_SMALL));

	return RegisterClassEx(&wcex);
}

//
//   FUNCTION: InitInstance(HINSTANCE, int)
//
//   PURPOSE: Saves instance handle and creates main window
//
//   COMMENTS:
//
//        In this function, we save the instance handle in a global variable and
//        create and display the main program window.
//
BOOL InitInstance(HINSTANCE hInstance, int nCmdShow)
{
   //HWND hWnd;
   hInst = hInstance; // Store instance handle in our global variable
   hWnd = CreateWindow(szWindowClass,(LPCSTR)"Location Tracking",WS_OVERLAPPEDWINDOW,CW_USEDEFAULT, 
	 	CW_USEDEFAULT,960,750,NULL,NULL,hInstance,NULL);
   if(!hWnd)
   {
      return FALSE;
   }

   ShowWindow(hWnd, nCmdShow);
   UpdateWindow(hWnd);

   HWND hStartButton = CreateWindow((LPCSTR)("button"),(LPCSTR)("Start"),
        WS_CHILD | WS_VISIBLE |BS_PUSHBUTTON |BS_TEXT,100, 50, 80, 20,                       
        hWnd,(HMENU)IDC_STARETBUTTON, hInstance,NULL); 
	 
   ShowWindow(hStartButton, nCmdShow);
   UpdateWindow(hStartButton);
 
   HWND hStopButton = CreateWindow((LPCSTR)("button"), (LPCSTR)"Stop",
	   WS_CHILD | WS_VISIBLE |BS_PUSHBUTTON |BS_TEXT, 400, 50, 80, 20,  
		hWnd,(HMENU) IDC_STOPBUTTON,hInstance,NULL); 

   ShowWindow(hStopButton, nCmdShow);
   UpdateWindow(hStopButton);

   return TRUE;
}

BOOL Drawpoint(HWND hWnd,fType xx,fType yy,COLORREF color)
{
    RECT rect;
	HDC hDC=GetDC(hWnd);
	//HBRUSH hBrush=CreateSolidBrush(RGB(144,238,144));  //green
	//HBRUSH hBrush=CreateSolidBrush(RGB(248,29,56)); //red
	HBRUSH hBrush=CreateSolidBrush(color); //red
	//coordinate value from user to window , the size of rect is 10
	//fType dis_coor_x = center_x + width*xx/80;
	//fType dis_coor_y = center_y - height*yy/80;
	fType dis_coor_x = center_x + width*xx/160;
	fType dis_coor_y = center_y - height*yy/160;

	rect.bottom=(long)(dis_coor_y - 3);
	rect.left=(long)(dis_coor_x -3);			
	rect.right=(long)(dis_coor_x  + 3);
	rect.top=(long)(dis_coor_y + 3);
	FillRect(hDC,&rect,hBrush);
	return TRUE;
}

//
//  FUNCTION: WndProc(HWND, UINT, WPARAM, LPARAM)
//
//  PURPOSE:  Processes messages for the main window.
//
//  WM_COMMAND	- process the application menu
//  WM_PAINT	- Paint the main window
//  WM_DESTROY	- post a quit message and return
LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam)
{
	int wmId, wmEvent;
	//PAINTSTRUCT ps;
	//HDC hdc;
	//the position of pic
	int x = 300;
	int y = 300;
	switch (message)
	{
		case WM_COMMAND:
		{
			wmId    = LOWORD(wParam);
			wmEvent = HIWORD(wParam);
			switch(LOWORD(wParam))     //get the value of start and stop button and to Start_Flag Stop_Flag
			{
				case IDC_STARETBUTTON:
				{
					//create sensor collecting thread
					isSensorThreadExit = false;
					create_sensor_thread();
					SetTimer(hWnd,1,SLEEP_TIME_SENCOLLECTIN,NULL); 
				}
				break;
				case IDC_STOPBUTTON:
				{
					isSensorThreadExit = true;
					KillTimer(hWnd, 1);
					sensor_save_data.clear();
				}
				break;
				case IDM_ABOUT:
					DialogBox(hInst, MAKEINTRESOURCE(IDD_ABOUTBOX), hWnd, About);
				break;
				case IDM_EXIT:
					DestroyWindow(hWnd);
				break;
			}
			break;
		}
		case WM_TIMER:
		{
			data_process();
		}
		break;
		case WM_PAINT:
		{
			draw_coordinate_axis();
		}
		break;
		case WM_DESTROY:
			PostQuitMessage(0);
		break;
		case UM_DISPLAY:
		{
			Drawpoint(hWnd,dis_x,dis_y,red);
		}
		break;
		default:
		return DefWindowProc(hWnd, message, wParam, lParam);
	}
	return 0;
}

// Message handler for about box. by default
INT_PTR CALLBACK About(HWND hDlg, UINT message, WPARAM wParam, LPARAM lParam)
{
	UNREFERENCED_PARAMETER(lParam);
	switch (message)
	{
		case WM_INITDIALOG:
			return (INT_PTR)TRUE;

		case WM_COMMAND:
			if (LOWORD(wParam) == IDOK || LOWORD(wParam) == IDCANCEL)
			{
				EndDialog(hDlg, LOWORD(wParam));
				return (INT_PTR)TRUE;
			}
		break;
	}
	return (INT_PTR)FALSE;
}

void draw_coordinate_axis()
{
	PAINTSTRUCT ps;
	HDC hdc;
	hdc = BeginPaint(hWnd, &ps);
	RECT rect_client;
	//RECT rect_window;
	GetClientRect(hWnd,&rect_client);  //set global varible to save the client area's width and 
	//height, so we can draw a coordinate according to them. At least should share with draw point section.
	height = rect_client.bottom - rect_client.top;
	width = rect_client.right - rect_client.left;
	center_y = (rect_client.bottom + rect_client.top)/2;
	center_x = (rect_client.right + rect_client.left)/2;
	//HDC hdc = GetDC(hWnd);
	HPEN hNewPen,hOldPen;
	hNewPen = CreatePen(PS_SOLID,1,RGB(0,255,0));//HBRUSH use method is the same
	hOldPen = (HPEN)SelectObject(hdc,hNewPen);       
	MoveToEx(hdc, rect_client.left+EDGE_WIDTH, rect_client.bottom-EDGE_WIDTH, NULL);
	LineTo(hdc,rect_client.left+EDGE_WIDTH,rect_client.top+EDGE_WIDTH);
	MoveToEx(hdc, rect_client.left+EDGE_WIDTH, rect_client.bottom-EDGE_WIDTH, NULL);
	LineTo(hdc,rect_client.right-EDGE_WIDTH,rect_client.bottom-EDGE_WIDTH);
	//int num_section = 8;
	int num_section = 16;
	double interval_section_height = (height - 2*EDGE_WIDTH)/num_section;
	double interval_section_width = (width - 2*EDGE_WIDTH)/num_section;
	//y axis
	int m_initx=10;
	int c = 3;
	char str[6];
	for(int i=0;i<num_section;i++)
	{
		MoveToEx(hdc, rect_client.left+EDGE_WIDTH, rect_client.bottom-EDGE_WIDTH-(i+1)*interval_section_height, NULL);
		LineTo(hdc,rect_client.left+EDGE_WIDTH+10, rect_client.bottom-EDGE_WIDTH-(i+1)*interval_section_height);
	}
	for(int i=0;i<=num_section;i++)
	{
		memset(str,0,6);
		//sprintf_s(str,sizeof(int),"%d",-40+m_initx*i); 
		sprintf_s(str,sizeof(int),"%d",-80+m_initx*i);
		int len;
		if((-80+m_initx*i)>0)
			len = c-1;
		else if(i == num_section/2)
			len = c-2;
		else
			len = c;
		TextOut(hdc,rect_client.left+EDGE_WIDTH-10, rect_client.bottom-EDGE_WIDTH-(i)*interval_section_height,str,len);
	}
	//x axis
	for(int i=0;i<num_section;i++)
	{
		MoveToEx(hdc, rect_client.left+EDGE_WIDTH+(i+1)*interval_section_width, rect_client.bottom-EDGE_WIDTH, NULL);
		LineTo(hdc,rect_client.left+EDGE_WIDTH+(i+1)*interval_section_width, rect_client.bottom-EDGE_WIDTH-10);
	}
	for(int i=0;i<=num_section;i++)
	{
		memset(str,0,6);
		//sprintf_s(str,sizeof(int),"%d",-40+m_initx*i); 
		sprintf_s(str,sizeof(int),"%d",-80+m_initx*i); 
		int len;
		if((-80+m_initx*i)>0)
			len = c-1;
		else if(i == num_section/2)
			len = c-2;
		else
			len = c;
		TextOut(hdc,rect_client.left+EDGE_WIDTH+(i)*interval_section_width, rect_client.bottom-EDGE_WIDTH+5,str,len);
	}
	//// TODO: Add any drawing code here... by default
	HBITMAP  bimp = (HBITMAP)LoadImage(NULL,"direction.bmp",
		IMAGE_BITMAP,100,100,LR_LOADFROMFILE);
	HDC hdcmem=CreateCompatibleDC(hdc);
	CreateCompatibleBitmap(hdc,100,100);
	SelectObject(hdcmem,bimp);
	BitBlt(hdc,rect_client.right-125,rect_client.top+25,100,100,hdcmem,0,0,SRCCOPY);
	fType display_xy[2];
	//tmp_sensor_param_test used for other purpose, i=0 is the first data of the queue
	if(display_data_x.empty())
	{
		Drawpoint(hWnd,0,0,blue);
	}
	else
	{
		for(int i=0; i < display_data_x.size(); i++)
		{
			display_xy[0] = display_data_x[i];
			display_xy[1] = display_data_y[i];
			Drawpoint(hWnd,display_xy[0],display_xy[1],blue);
		}
	}
	SelectObject(hdc,hOldPen);
	DeleteObject(hNewPen);
	EndPaint(hWnd, &ps);
	ReleaseDC(hWnd,hdc);
}
