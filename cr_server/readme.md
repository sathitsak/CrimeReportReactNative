/*Pre-requisites*/
MySQL
Maven
Java 8
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
/*MySQL*/

MySQL on Windows 10

Download MySQL Installer from http://dev.mysql.com/downloads/installer/ and execute it.
Choose the appropriate Setup Type for your system. Typically you will choose Developer Default to install MySQL server and other MySQL tools related to MySQL development, helpful tools like MySQL Workbench. Or, choose the Custom setup type to manually select your desired MySQL products.
Choose the appropriate Setup Type for your system. Typically you will choose Developer Default to install MySQL server and other MySQL tools related to MySQL development, helpful tools like MySQL Workbench. Or, choose the Custom setup type to manually select your desired MySQL products.
MySQL on Mac OS X

Download the disk image (.dmg) file (the community version is available here https://dev.mysql.com/downloads/mysql/) that contains the MySQL package installer. Double-click the file to mount the disk image and see its contents.
Double-click the MySQL installer package from the disk. It is named according to the version of MySQL you have downloaded. For example, for MySQL server 5.7.24 it might be named mysql-5.7.24-osx-10.13-x86_64.pkg.
The initial wizard introduction screen references the MySQL server version to install. Click Continue to begin the installation.
The MySQL community edition shows a copy of the relevant GNU General Public License. Click Continue and then Agree to continue.
From the Installation Type page you can either click Install to execute the installation wizard using all defaults, click Customize to alter which components to install (MySQL server, Preference Pane, Launchd Support -- all enabled by default).
Click Install to begin the installation process.
After a successful installation, the installer displays a window with your temporary root password. This cannot be recovered so you must save this password for the initial login to MySQL. For example:
Summary is the final step and references a successful and complete MySQL Server installation. Close the wizard.
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
/*Maven*/

Maven on MacOS 

Download the last version of Maven (binary) on http://maven.apache.org/download.cgi
Extract the zip where you want on your file system.
For use Maven as command line you need to create a symbolic link. Open the Terminal and launch this command:
sudo ln -s /path_to_maven_folder/bin/mvn /usr/bin/mvn
Check it with this command:
mvn --version


Maven on Windows



1. JDK and JAVA_HOME

Make sure JDK is installed, and “JAVA_HOME” variable is added as Windows environment variable.

2. Download Apache Maven

Visit http://maven.apache.org/download.cgi download the Maven zip file, for example : apache-maven-3.2.2-bin.zip. Unzip it to the folder you want to install Maven.

Assume you unzip to this folder – C:\Program Files\Apache\maven

Note
That’s all, just folders and files, installation is NOT required.

3. Add M2_HOME and MAVEN_HOME

Add both M2_HOME and MAVEN_HOME variables in the Windows environment, and point it to your Maven folder.

M2_HOME or MAVEN_HOME
Maven document said add M2_HOME only, but some programs still reference Maven folder with MAVEN_HOME, so, it’s safer to add both.

4. Add To PATH

Update PATH variable, append Maven bin folder – %M2_HOME%\bin, so that you can run the Maven’s command everywhere.

5. Verification

Done, to verify it, run mvn –version in the command prompt.
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
In MySQL server: -
have a user: - 'root'
with password: - 'crbilbydb'
and DB name: - 'cr_db'

