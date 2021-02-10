# ShoutOut

## An introduction
ShoutOut is a native Android app to enable the clubs, departments, and regional assocs of BPHC to post announcements which can be seen and interacted with by the general body.
Functionality:

Built to allow access only to BITS email addresses.

Feature set for club accounts : adding posts, viewing a post’s analytics (number of views, engagements, reacts), editing and deleting posts.

Feature set for student accounts: liking and reacting to posts , viewing a post’s edit history, commenting on posts and replying to comments.

## Tech stack 

*Repository based MVVM architecture employing kotlin*

*JetPack features*
    
* LiveData - notify domain layer data to views.
* Lifecycle - dispose of observing data when lifecycle state changes.
* ViewModel - UI related data holder, lifecycle aware.

## Open Source libraries used
* Picasso 
* Paging 3.0  
* CircleImageView 
