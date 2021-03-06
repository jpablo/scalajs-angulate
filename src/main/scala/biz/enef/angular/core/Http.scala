// -   Project: scalajs-angulate (https://github.com/jokade/scalajs-angulate)
// Description: API for Angular $httpProvider
//
// Based on https://github.com/greencatsoft/scalajs-angular .. angularjs/core/Http.scala
//
// Copyright (c) 2015 Johannes Kastner <jokade@karchedon.de>
//               Distributed under the MIT License (see included file LICENSE)
package biz.enef.angular.core

import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, CanAwait, TimeoutException, Future}
import scala.reflect.macros.whitebox
import scala.language.experimental.macros
import scala.scalajs.js
import scala.scalajs.js.UndefOr
import scala.util.Try

trait HttpService extends js.Object {
  def get[T](url: String): HttpPromise[T] = ???
  def get[T](url: String, config: HttpConfig): HttpPromise[T] = ???
  def post[T](url: String): HttpPromise[T] = ???
  def post[T](url: String, data: js.Any): HttpPromise[T] = ???
  def post[T](url: String, data: js.Any, config: HttpConfig): HttpPromise[T] = ???
  def jsonp[T](url: String, config: HttpConfig): HttpPromise[T] = ???
  def put[T](url: String): HttpPromise[T] = ???
  def put[T](url: String, data: js.Any): HttpPromise[T] = ???
  def put[T](url: String, data: js.Any, config: HttpConfig): HttpPromise[T] = ???
  def delete[T](url: String): HttpPromise[T] = ???
  def delete[T](url: String, data: js.Any): HttpPromise[T] = ???
  def delete[T](url: String, data: js.Any, config: HttpConfig): HttpPromise[T] = ???
}

trait HttpConfig extends js.Object {
  var method: String = ???
  var url: String = ???
  var params : js.Dictionary[js.Any] = ???
  var data : js.Any = ???
  var headers : js.Dictionary[js.Any] = ???
  var xsrfHeaderName : String = ???
  var xsrfCookieName : String = ???
  var transformResponse: js.Array[js.Function2[js.Any, js.Any, js.Any]] = ???
  var transformRequest: js.Array[js.Function2[js.Any, js.Any, js.Any]] = ???
  var cache: js.Any = ???
  var withCredentials: Boolean = ???
  var timeout: js.Any = ???
  var responseType : String = ???
}

object HttpConfig {
  import js.Dynamic.literal

  def apply[A](params: (String,A)*) : HttpConfig = literal(params= js.Dictionary(params:_*)).asInstanceOf[HttpConfig]

  def apply(method: String = null,
            url: String = null,
            params: js.Dictionary[js.Any] = null,
            data: js.Any = null,
            headers: js.Dictionary[js.Any] = null,
            xsrfHeaderName : String = null,
            xsrfCookieName : String = null,
            transformResponse: js.Array[js.Function2[js.Any, js.Any, js.Any]] = null,
            transformRequest: js.Array[js.Function2[js.Any, js.Any, js.Any]] = null,
            cache: js.Any = null,
            withCredentials: Boolean = false,
            timeout: js.Any = null,
            responseType : String = null) : HttpConfig = {
    literal(method = method, url=url, params=params, data=data, headers=headers,
            xsrfCookieName= xsrfCookieName, xsrfHeaderName= xsrfHeaderName,
            transformRequest=transformRequest, transformResponse=transformResponse,
            cache=cache, withCredentials=withCredentials, timeout=timeout,
            responseType=responseType).asInstanceOf[HttpConfig]
  }

}

trait HttpPromise[T] extends js.Object {
  def success(callback: js.Function): this.type
  def success(callback: js.Function1[js.Any, Unit]): this.type
  def success(callback: js.Function2[js.Any, Int, Unit]): this.type
  def success(callback: js.Function3[js.Any, js.Any, Int, Unit]): this.type
  def success(callback: js.Function4[js.Any, Int, js.Any, js.Any, Unit]): this.type
  def success(callback: js.Function5[js.Any, Int, js.Any, js.Any, js.Any, Unit]): this.type
  def error(callback: js.Function): this.type
  def error(callback: js.Function1[js.Any, Unit]): this.type
  def error(callback: js.Function2[js.Any, Int, Unit]): this.type
  def error(callback: js.Function3[js.Any, js.Any, Int, Unit]): this.type
  def error(callback: js.Function4[js.Any, Int, js.Any, js.Any, Unit]): this.type
  def error(callback: js.Function5[js.Any, Int, js.Any, js.Any, UndefOr[String], Unit]): this.type

  var `then`: js.Function3[js.Function,js.Function,js.Function,HttpPromise[T]] = ???
  //var `then`: js.Function3[js.Function1[T,Unit],js.Function,js.Function,Unit] = ???

  //------------------------- ANGULATE ENHANCEMENTS --------------------------
  /**
   * The provided callback will be called when a successful response is available
   * (response status codes between 200 and 299 are considered a success status).
   *
   * @note This is a scalajs-angulate enhancement. The response data provided to
   *       the callback will be cast to the specified type T.
   *
   * @param callback Callback function that will receive the response data as its first argument
   * @tparam T Type of the response data object
   */
  //def success[T](callback: (T) => Unit) : this.type = macro impl.HttpPromiseMacros.onSuccessWithType[T]

  //def onSuccess(pf: PartialFunction[Any,Unit]) : Unit = macro impl.HttpPromiseMacros.onSuccess

  def onSuccess(f: T=>Unit) : HttpPromise[T] = macro impl.HttpPromiseMacros.onSuccess

  def onComplete(f: Try[T]=>Unit) : HttpPromise[T] = macro impl.HttpPromiseMacros.onComplete

  def onFailure(f: (HttpError)=>Unit) : HttpPromise[T] = macro impl.HttpPromiseMacros.onFailure

  def map[U](f: T=>U) : HttpPromise[U] = macro impl.HttpPromiseMacros.map

  /**
   * Returns a Scala Future for this HttpPromise.
   *
   * @note This is a scalajs-angulate enhancement.
   */
  def future: Future[T] = macro impl.HttpPromiseMacros.future
}

class HttpError(msg: String, val status: Int) extends RuntimeException(msg)

