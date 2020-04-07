# 交大國樂小助手 NCTUCMC
### 簡介 Introduction

初次深入接觸Android development，幫國樂社寫一個琴房借用系統，順便嘗試達成理想的功能：
This is may first attempt on Android development, meant for constructing a practice room reservation system. Some features are also included or in development:

- 社團聊天室(已完成)、群組聊天室 Club member chatroom (completed), group chatroom (dev)
- 重要事件通知(已完成) Club notifications (completed)
- 社員調查系統：填寫練出席表、活動時間投票、意見投票等等 (dev)

Google Play: [Link](https://play.google.com/store/apps/details?id=com.raymond210129.nctucmc)

### 程式縮圖 Pictures
![](https://i.imgur.com/NUHCqTL.png | width=250)
![](https://i.imgur.com/91MxufR.png | width=250)
![](https://i.imgur.com/ntAdZA4.png | width=250)
![](https://i.imgur.com/mhAVRCy.png | width=250)
![](https://i.imgur.com/gFB4Fq9.png | width=250)




### 更新記錄 Update logs

#### alpha-1.3.2
- change log:
 1. Comments sent by user itself will be shown at right side in conversation box and re-colored.
 2. fix the datepicker layout in booking screen that is unscrollable on lower api devices (older than android M).
 3. force all fragments pre-render when launching the app and not to recreate the fragment view to avoid laggy switching.

#### alpha-1.3.1
- change log:
 1. Activiate the multicast feature for bulletin system.
 2. Reverse the order of bulletin view to let the newer anouncement keep at top.
 3. Add some command input features in chatroom to replace the invisible NavigationView temporarily that cause by the unsolved bug on api 23.
 4. The first uploaded version on Google Play.

#### alpha-1.3.0:
- change log:
 1. Implement bulletin system with Firebase realtime database, user can post announcements with different kinds of ranks, and send notifications with different priority. several features will be added correspondingly in near future including multicast posts, user group profile, and scheduled notifications.
 2. rearrange the calender selector to adapt in different screen sizes.
 3. Replacing the original BottomNavigationView to Third party package AHBottomNavigation and Clan's FloatActionButton. background color transmition will be acted more reasonably.

#### alpha-1.2.0:
- change log:
 1. Implement notification service with Firebase realtime database and Firebase functions, which will be fired when users send messages in chatroom.
 2. Reallocate the messages data structure in database, apps with old version may not work properly.

- bugs:
 1. Notification traps can't be collapsed while using collapseKey and tag. looking for other alternative method. **-- unsolve --**

#### alpha-1.1.2:
- change log:
 1. Minor change: reservation layout in main page has been rearranged to adapt different screen size more correctly.
 2. Bug fixed: reason of ANR in api 23 (6.0) has been recognized and solved, because of the fact that BottomNavigationView is contain in new released library which is uncompatable with api 23.

- bugs:
 1. Contents of NavigationView do not show in application on android api 23, user is unable to logout or change password. **-- unsolve --**

#### alpha-1.1.1: 

- change log:
 1. Minor change: actionBar, navigationView & statusBar color changes when fragments are switched. 

#### alpha-1.1.0: 
- change log:
 1. Group chatroom has been realized with google Firebase. 
 
- bugs:
 1. Application crashes on android api 23 (6.0), error log:
 > android.view.InflateException: Binary XML file line #28: Binary XML file line #28: Error inflating class android.support.design.widget.BottomNavigationView

   ***-- solved --***
 


#### alpha-1.0.0: 
- change log:
 1. Initial commit
 2. Simple login system: using NCTUCS database system & apache on CS workstation
 3. Club practice room reservation interface with simple race prevention
 
- bugs:
 1. The first time slot each day in reservation system does not work properly. may cause by 0 recogintion probem by php.  **-- unsolve --**






