This is my todo items app.

The items are stored in SQLite. The database is managed by TodoAppDatabase that uses the interface TableProvider to create the tables it needs. TodoItemsTableProvider implements the interface and defines the todo items table with fields for the string value, created at, updated at, priority, and due date. Todo items are managed by TodoItemsHandler which is a singleton.

When you boot up the app it shows no todo items. There is a custom action bar with a logo on the left and two buttons on the right.

![alt tag](https://github.com/ebaumstarck/todoapp_android/blob/master/demo/screenshot1.png)

You add items by typing in text at the bottom and clicking "Add Item." They are sorted by name at first.

<screenshot2>

You can long click on an item to open the edit activity. This has a custom action bar with a back button. You can set the priority on the task (low, regular, high) and set a due date which is optional.

<screenshot3>

The due date is set with a date picker.

<screenshot4>

Once you set due date or change priority it shows on the main activity.

<screenshot5>

If you click the sort menu button on the action bar it brings up a list of sort options. Sort by important puts the high priority item first.

<screenshot6>

Click on the edit icon does an animation that fades out the priority and due dates and fades in controls to delete or edit each todo item. The "Edit" button does the same as long clicking on the todo item.

<screenshot7>

Click "Delete" brings up a confirmation dialog:

<screenshot8>

Click "OK" and the item gets removed from the display and the SQLite database:

<screenshot9>

All strings are kept as resources in "res/values/strings.xml" and all dimensions are kept as resources in "res/values/dimens.xml/dimens.xml".