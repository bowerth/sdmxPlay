[![Build Status](https://travis-ci.org/bowerth/sdmxPlay.svg?branch=master)](https://travis-ci.org/bowerth/sdmxPlay)

SDMX Play application
=================================

Scala Play application using [SDMX](https://github.com/amattioc/SDMX) Java functions and [dygraphs](https://github.com/danvk/dygraphs) timeseries visualization.

This application retrieves time series available from SDMX APIs. The SDMX query can either be specified through the user interface or by modification of the URL. The SDMX provider is henceforth contacted with the query and in case of existing time series returns information transformed into a JavaScript data format for display and download.

To facilitate defining the query, available dimension members can be displayed for the selected flow and provider.

![ECB EXR time series](assets/screenshot-ecb-exr-a-q-m.png)

## Example Queries

- [Daily Exchange Rates USD, GBP to EUR since 2010](http://sdmx.rdata.work/ECB/EXR.D.USD+GBP.EUR.SP00.A/?start=2010)

## Running in production mode

To run the application in production, start by creating a jar containing the app and the dependencies by running:

    $ activator dist

This will create a zip archive called `sdmxplay-1.0-SNAPSHOT.zip` with the application itself, its dependencies and a shell script to run it, in the directory `target/universal/`.

To run the application in production, just unzip the archive and run  the shell script `./sdmxplay-1.0-SNAPSHOT/bin/sdmxplay`. The application runs on port 9000 by default.

It is likely that you want the application to listen to port 80, the default port for HTTP, rather than on port 9000. To run it on port 80, you can specify the port explicitly when starting the application:

    $ sudo bash ./sdmxplay-1.0-SNAPSHOT/bin/sdmxplay -Dhttp.port=80

Since 80 is a privileged port, you will need to run this with root access. Alternatively, to avoid having to run your application as root (this is preferable), set up a traditional web server like Apache to forward requests to port 9000. To do this on a Ubuntu instance, start by installing Apache and `mod_proxy` using:

    sudo apt-get install -y apache2
    sudo apt-get install -y libapache2-mod-proxy-html libxml2-dev
    sudo a2enmod proxy proxy_ajp proxy_http rewrite deflate headers proxy_balancer proxy_connect proxy_html

You can then tell Apache to forward connections to port 9000 by inserting the following lines in `/etc/apache2/apache2.conf`:

    <VirtualHost *:80>
        ProxyPreserveHost On
        ServerName app.scala4datascience.com
        ProxyPass  /excluded !
        ProxyPass / http://127.0.0.1:9000/
        ProxyPassReverse / http://127.0.0.1:9000/
    </VirtualHost>

Restart the Apache server with:

    $ sudo service apache2 restart

