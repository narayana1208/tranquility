/*
 * Tranquility.
 * Copyright (C) 2013, 2014  Metamarkets Group Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.metamx.tranquility

import com.metamx.common.scala.Predef._
import com.twitter.util.{Duration => TwitterDuration}
import org.jboss.netty.handler.codec.http.DefaultHttpRequest
import org.jboss.netty.handler.codec.http.HttpMethod
import org.jboss.netty.handler.codec.http.HttpRequest
import org.jboss.netty.handler.codec.http.HttpVersion
import org.joda.time.Duration
import org.scala_tools.time.Implicits._

package object finagle
{
  implicit def jodaDurationToTwitterDuration(duration: Duration): TwitterDuration = {
    TwitterDuration.fromMilliseconds(duration.millis)
  }

  lazy val FinagleLogger = java.util.logging.Logger.getLogger("finagle")

  def HttpPost(path: String) = {
    new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, path) withEffect {
      req =>
        decorateRequest(req)
    }
  }

  def HttpGet(path: String) = {
    new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path) withEffect {
      req =>
        decorateRequest(req)
    }
  }

  private[this] def decorateRequest(req: HttpRequest) = {
    // finagle-http doesn't set the host header, and we don't actually know what server we're hitting
    req.headers.set("Host", "127.0.0.1")
    req.headers.set("Accept", "*/*")
  }
}
