package com.adsamcik.recycler.adapter.implementation.multitype

/**
 * Not registered class exception.
 */
class NotRegisteredException(message: String) : Exception(message)

/**
 * Already registered class exception.
 */
class AlreadyRegisteredException : Exception {
	constructor(message: String?) : super(message)
	constructor(message: String?, cause: Throwable?) : super(message, cause)
}
