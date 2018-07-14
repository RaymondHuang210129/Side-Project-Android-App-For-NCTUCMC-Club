# 交大國樂小助手 NCTUCMC
### 簡介 Intro
初次深入接觸Android development，幫國樂社寫一個琴房借用系統，順便嘗試達成理想的功能：
- 社團聊天室、群組聊天室
- 重要事件通知
- 社員調查系統：填寫練出席表、活動時間投票、意見投票等等


### 更新記錄 Update logs
##### alpha-1.1.1: 
- change log:
 1. Minor change: actionBar, navigationView & statusBar color changes when fragments are switched. 

##### alpha-1.1.0: 
- change log:
 1. Group chatroom has been realized with google Firebase. 
 
- bugs:
 1. Application crashes on android api 23 (6.0), error log:
 > android.view.InflateException: Binary XML file line #28: Binary XML file line #28: Error inflating class android.support.design.widget.BottomNavigationView

   **-- unsolve --**
 

##### alpha-1.0.0: 
- change log:
 1. Initial commit
 2. Simple login system: using NCTUCS database system & apache on CS workstation
 3. Club practice room reservation interface with simple race prevention
 
- bugs:
 1. The first time slot each day in reservation system does not work properly. may cause by 0 recogintion probem by php.  **-- unsolve --**






