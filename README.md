DAOLite
=======

A generic & lite DAO for Android



Description
===========
You can use for your Android projects ORMLite, GreenDAO or another ORM. You 
can make your own DAOs or your SQL queries.
If you do not need the powerful of an ORM and you have no time to waste 
creating your own DAOs, this is your solution.

In all the projects we have worked required at least this level of development. 
For a basic project that will be enough. 

The module does not use reflectivity so massive, only a few calls in order 
to be much more efficient than models with reflectivity. Neither requires 
extending an abstract class so that your models can be as complex as you want.

The main classes are:
* GenericDAO, the DAO headers.
* GenericDaoImpl, the DAO implementation.
* DBEntity, interface to implement in your models.



Documentation
=============
You have the [JavaDoc](GenericDAOLite/doc/javadoc) files on doc/javadoc directory, 
and [UML](GenericDAOLite/doc/uml) diagrams on doc/uml directory.

Class diagram
-------------
![Class diagram](GenericDAOLite/doc/uml/ClassDiagram.png?raw=true "Class diagram")

Sample model diagram
--------------------
![Sample model diagram](GenericDAOLite/doc/uml/BusinessModelDiagram.png?raw=true "Sample model diagram")



Contact
=======
Any help or comment to improve this model to make it more simple to understand 
or obtain a better performance will be gratefully accepted.



Developed by
============
R2B Apps

If you use DAOLite code in your application you should inform R2B Apps about it ( *email: r2b.apps[at]gmail[dot]com* ) like this:
> **Subject:** DAOLite usage notification<br />
> **Text:** I use Universal Image Loader &lt;lib_version> in &lt;application_name> - http://link_to_google_play.
> I [allow | don't allow] to mention my app in section "Applications using DAOLite" on GitHub.

Also We will be grateful if you mention DAOLite in application UI with string **"Using DAOLite (c) 2014, R2B Apps"** (e.g. in some "About" section).



License
=======
The MIT License (MIT)

Copyright (c) 2014 R2B Apps

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
