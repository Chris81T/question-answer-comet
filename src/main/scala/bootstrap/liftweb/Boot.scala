package bootstrap.liftweb

import net.liftweb._

import common._
import http._
import sitemap._

import code.snippet._

/**
 * FLEX MENU BUILDER have a look for it -- Diego Medina
 */


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    // where to search snippet
    LiftRules.addToPackages("code")

    // Build SiteMap
    val entries = List(
      Menu.i("Home") / "index")

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMap(SiteMap(entries: _*))

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    LiftRules.jsArtifacts = net.liftweb.http.js.jquery.JQueryArtifacts

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    LiftRules.htmlProperties.default.set((r: Req) => new Html5Properties(r.userAgent))
    
    // HEARTBEAT ...
//    LiftSession.onBeginServicing ::= ((s, req) => println("++++++++++++++++++++++++++++ ON BEGIN SERVICING"))
//    LiftSession.onSessionActivate ::= ((s) => println("++++++++++++++++++++++++++++ ON SESSION ACTIVATE"))
//
//    LiftSession.onEndServicing ::= ((s, req, res) => println("---------------------------- ON END SERVICING"))
//    LiftSession.onSessionPassivate ::= ((s) => println("---------------------------- ON SESSION PASSIVATE"))
  }
}
