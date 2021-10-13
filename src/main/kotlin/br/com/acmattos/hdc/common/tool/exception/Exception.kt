package br.com.acmattos.hdc.common.tool.exception

/**
 * @author ACMattos
 * @since 30/06/2020.
 */
class InternalServerErrorException(message: String, th: Throwable): Exception(message, th)

/**
 * @author ACMattos
 * @since 16/08/2021.
 */
class NotFoundException(message: String): Exception(message)