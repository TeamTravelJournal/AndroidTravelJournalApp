# Group Project - *Travel Journal*

**Travel Journal** is an Android app that empowers users to become photo journalists. Whether off to far away cities, landmarks, or the nearest coffee shop, Travel Journal is the users' companion for photo journaling their adventures. Journaling is as simple as snapping a photo. In a writing mood? Easily add captions. Want to explore? Browse other peoples' travels be specifying a location or just scroll through posts on a map. Whether you like to personally or virtually explore, Travel Journal has you covered.

Time spent: **X** hours spent in total

## User Stories

Login
* [x] Users can login via Facebook
* [x] Ken Burns effect

Browse posts: 3 ways to find travel content
* [x] Browse recent content: timeline
* [x] Browse nearby content: map
* [x] Browse location specific content: search bar - results visible on timeline list view or map view

Search
* [x] Can search for posts based on location
* [x] Search results can be seen either as list view or map view

Timeline List View
* [x] Shows a list of posts, each Post contains 
  * [x] Photo
  * [x] Caption
  * [x] City
  * [x] Relative timestamp
  * [x] Profile picture of the creator
  * [x] Like button: Heart icon to like
* [x] Long click to post or click to heart icon results in liking the post along with a heart animation
* [x] Can dive into post detail view on click to post
* [x] Hiding toolbar
* [x] Floating action button to create post
* [x] Swipe to refresh page
* [x] Endless scroll

Timeline Map View
* [x] Each post is a pin
* [x] When a pin is clicked, thumbnail of post photo pops up and can dive into post detail view
* [x] Dynamically loads the pins: Zooming in/out of the map or moving the map would result new pins to show on new visible regions of the map 

Profile page(of current user or any other user)
* [x] Profile picture
* [x] List of followers and list of people they are following shown in tabs 
* [x] Follow icon to follow the user that results in ripple effect
* [x] Collapsing toolbar
* [x] Parallax effect
* [x] All posts published by the user can be seen in list format
* [x] All posts published by the user can be seen in map format (travel map for the user)
* [x] Long click on travel map for the user: makes animation showing the travel route of that user

Detail Page of a Post:
* [x] Contains photo(s), caption, profile photo, city, time
* [x] Number of likes, button to like, heart animation on like action
* [x] Number of followers, button to follow/unfollow, smiley animation on follow action 
* [x] Share post
* [x] Map with pin showing post location
* [x] Map is clickable and can be expanded to full screen, where photo is shown at the exact location on map
* [ ] Image is clickable and can be expanded to full screen
* [x] Shared element transitions between detail and timeline
* [x] View pager cubicle transformation between photos of a post when you slide in between them
* [x] Parallax effect
* [x] Last 3 comments can be seen on detail page

Comments Page
* [x] Reached by clicking into coments icon on detail page
* [x] Shows all comments for the post along with profile picture and user name
* [x] Ability to add a new comment and see the new comments immediately on comments page and detail page

Creation
* [x] Reached by clicking on FAB on timeline, easy to use design
* [x] Contains caption
* [x] Location from gps
* [x] Photo from camera
* [x] User is directed to Detail Page upon creation of post
* [x] User can click cross icon to remove the photo and click to camera icon to take another one

Other:
* [x] Push notification: User gets a notification when a new post is added
* [x] Clicking on the notification directs user to detail page of that post
* [x] In-app notification: If a new post is added when app is open, a notification pops up to let user know there are new posts, clicking on the pop up takes user to the top where new stories are.
* [x] Handle screen rotations
* [x] Activity transitions
* [x] Show progress bar for network calls


**To-Do**
* [ ] Photo can be chosen from gallery to create post 
* [ ] 3D image integration like yelp’s monocle 
* [ ] Log out
* [ ] Direct messages so users can get travel tips from each other
* [ ] Reviews - integration with Yelp or TripAdvisor

**Extension**
* [ ] Users can login via email with a custom username and password
* [ ] Nice video/intro when logging in for first time  
* [ ] See Popular posts on timeline
* [ ] Auto create collage and trips from your gallery by grouping location and date and time.
* [ ] Add Video
* [ ] Adding filters to images like instagram
* [ ] Invite contacts and facebook friends to the app
* [ ] Google Street view/earth like integration
* [ ] Local caching/ prefetching for offline use and faster UI
* [ ] Update your journey while offline and sync it when you have the Internet connection.
* [ ] More explicit trip creation
* [ ] Posts are organized in trips
  * [ ] Can add posts to trips
  * [ ] Browse trips on timeline
  * [ ] Browse trips on map
* [ ] Add Posts/Trips to your Wish list


## WireFrames

Wireframes can be found [here](https://github.com/TeamTravelJournal/AndroidTravelJournalApp/blob/master/wireframesTravelJournal.pdf).

## Gif Walkthrough

Sprint-3: June 24th 2015

<img src='https://github.com/TeamTravelJournal/AndroidTravelJournalApp/blob/master/group_project_sprint_3_01.gif' title='Sprint 3 Walkthrough' width='' alt='Video Walkthrough' />

Login Page and notifications:

<img src='https://github.com/TeamTravelJournal/AndroidTravelJournalApp/blob/master/group_project_sprint_3_02.gif' title='Sprint 3 Walkthrough-Cont' width='' alt='Video Walkthrough' />

Sprint-2: June 17th 2015

<img src='https://github.com/TeamTravelJournal/AndroidTravelJournalApp/blob/master/group_project_sprint_2.gif' title='Sprint 2 Walkthrough' width='' alt='Video Walkthrough' />

Sprint-1: June 10th 2015

<img src='https://github.com/TeamTravelJournal/AndroidTravelJournalApp/blob/master/group_project_sprint_1.gif' title='Sprint 1 Walkthrough' width='' alt='Video Walkthrough' />

## License

    Copyright [2015] [Srivats Jayaram, Esra Kucukoguz, Richard Pon]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
