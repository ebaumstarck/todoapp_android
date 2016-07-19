This is my todo items app.

<img src="http://g.recordit.co/gM01EbWigV.gif" width="200">

# Architecture #

The items are stored in SQLite. The database is managed by TodoAppDatabase that uses the interface TableProvider to create the tables it needs. TodoItemsTableProvider implements the interface and defines the todo items table with fields for the string value, created at, updated at, priority, and due date. Todo items are managed by TodoItemsHandler which is a singleton.

# Step by step demo #

When you boot up the app it shows no todo items. There is a custom action bar with a logo on the left and two buttons on the right.

<img src="https://github.com/ebaumstarck/todoapp_android/blob/master/demo/screenshot1.png" width="200">

You add items by typing in text at the bottom and clicking "Add Item." They are sorted by name at first.

<img src="https://github.com/ebaumstarck/todoapp_android/blob/master/demo/screenshot2.png" width="200">

You can long click on an item to open the edit activity. This has a custom action bar with a back button. You can set the priority on the task (low, regular, high) and set a due date which is optional.

<img src="https://github.com/ebaumstarck/todoapp_android/blob/master/demo/screenshot3.png" width="200">

The due date is set with a date picker.

<img src="https://github.com/ebaumstarck/todoapp_android/blob/master/demo/screenshot4.png" width="200">

Once you set due date or change priority it shows on the main activity.

<img src="https://github.com/ebaumstarck/todoapp_android/blob/master/demo/screenshot5.png" width="200">

If you click the sort menu button on the action bar it brings up a list of sort options. Sort by important puts the high priority item first.

<img src="https://github.com/ebaumstarck/todoapp_android/blob/master/demo/screenshot6.png" width="200">

Click on the edit icon does an animation that fades out the priority and due dates and fades in controls to delete or edit each todo item. The "Edit" button does the same as long clicking on the todo item.

<img src="https://github.com/ebaumstarck/todoapp_android/blob/master/demo/screenshot7.png" width="200">

Click "Delete" brings up a confirmation dialog:

<img src="https://github.com/ebaumstarck/todoapp_android/blob/master/demo/screenshot8.png" width="200">

Click "OK" and the item gets removed from the display and the SQLite database:

<img src="https://github.com/ebaumstarck/todoapp_android/blob/master/demo/screenshot9.png" width="200">

All strings are kept as resources in "res/values/strings.xml" and all dimensions are kept as resources in "res/values/dimens.xml/dimens.xml".
