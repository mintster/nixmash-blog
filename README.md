NixMash Blog
==========================

NixMash Blog is a Java Spring Web Bootstrap 3 application based on [NixMash Spring](https://github.com/mintster/spring-data). You will soon see it in action at [NixMash,](http://nixmash.com) currently a WordPress Blog. NixMash Blog was designed to run on Linux.

If you wish to move your WordPress Blog to the NixMash Blog application, a WordPress-to-NixMash Blog migration application is available on [GitHub here.](https://github.com/mintster/wp-nixmashspring-migrator)

##About NixMash Blog## 

It wasn't our intention to build another Blog Engine, but the blogging capability in [NixMash Spring](https://github.com/mintster/spring-data) made the idea of creating a standalone blogging application became a non-brainer for several reasons:

1. Darn good basic blogging features like Tags, Categories and RSS
2. Solr Post search
3. Variety of Post Formats like Links, Standard Posts, Multiple and Single Post display
4. User support features like Password Reset and Social Login with Facebook, Google and Twitter
5. Built in 100% Java Spring, with Thymeleaf View Rendering and Bootstrap 3
6. Easily customizable
7. Can be used as a basis for client applications
8. NOT WordPress (which is awesome), but with the worries of hacking and those frequent site updates
9. Deployed as a WAR with embedded Jetty Web Server. Can be configured to use embedded H2Database and embedded Solr as well.
10. Fully documented on detailed blog posts on [NixMash.com](http://nixmash.com) and elsewhere
11. Free and Open Source, licensed under GNU GPL v3

##Installation##

Download the NixMash Blog source code from GitHub and build the application in your IDE or in Gradle. For Gradle there is a Build Script in `/install/sh/buildAll.sh` which will build this multi-module app.

Other setup steps *BEFORE BUILDING THE APPLICATION THE FIRST TIME* are:

1. Install Solr Server. There are several posts on [NixMash.com](http://nixmash.com) on how to do this with points specific to this app or **NixMash Spring**. To run Solr tests, populate the HTTP Solr Server AND Embedded Solr server with the `/install/solr/refreshSolr.sh` script. This will also confirm that Solr is property configured.
2. Run the MySQL Setup script in `/install/sql/mysqlsetup.sql.`  Then run `/install/sql/initialize.sql.` *IF YOU ARE MIGRATING A WORDPRESS BLOG, FOLLOW THE MIGRATOR APP SQL SETUP INSTRUCTIONS.* If you decide to setup **NixMash Blog** and THEN decide to migrate a WordPress Blog the sequence would be (1) `mysqlsetup.sql` (2) Migrate the WordPress Blog data, then (3) run `initialize.sql.`
3. Configure `JPA/src/main/resources/application.properties` property `blog.properties.file.path.` This determines the path to your external `blog.properties` file. 
4. Update appropriate properties in the external `blog.properties` file.


##Administration and Users##

The site is configured to allow user logins through both Site Registration and the Social Services of Facebook, Google and Twitter. You can disable user account creation with the `blog.loginEnabled` property in the external `blog.properties` file.

###Admin User Account###

The default user is "Ken Watts" (fictitious) with the following account credentials:

**USERNAME:** ken
**PASSWORD:** password

Please remember to change the username and password before going live...

##Other Configuration Matters##

- For Development the app is configured to use port 9000, or *http://localhost:9000*
- NixMash Blog is configured to use resources from a file path specified in the external `blog.properties` file. For development and testing the app on http://localhost:9000, there is a script to create a softlink to this file path root in `/install/linkbBuild.sh.`
- The best way to package the application for deployment is with Gradle. The command would be

```bash
$ gradle mvc:clean mvc:bootRepackage
```

##License##

NixMash Blog is Open Source. We only require that you abide by the terms of the GNU GPL v3 License.



