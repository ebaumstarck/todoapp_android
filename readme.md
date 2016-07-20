# Pre-work - *TodoApp*

**TodoApp** is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: **Emma Baumstarck**

Time spent: **8** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **successfully add and remove items** from the todo list
* [x] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [x] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [x] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [x] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [x] Add support for completion due dates for todo items (and display within listview item)
* [partial] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
** My app uses a DialogFragment to select the due date for the todo item
* [x] Add support for selecting the priority of each todo item (and display in listview item)
* [x] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [x] Custom action bars for the main activity and edit activity. They have sort and edit actions.

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img
  src="http://g.recordit.co/gM01EbWigV.gif"
  width="200"
  title="Video Walkthrough"
  alt="Video Walkthrough">

## Notes

### Architecture ###

The items are stored in SQLite. The database is managed by TodoAppDatabase that uses the interface TableProvider to create the tables it needs. TodoItemsTableProvider implements the interface and defines the todo items table with fields for the string value, created at, updated at, priority, and due date. Todo items are managed by TodoItemsHandler which is a singleton.

### Step by step demo ###

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

## License

    Copyright 2016 Emma Liu Baumstarck

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
