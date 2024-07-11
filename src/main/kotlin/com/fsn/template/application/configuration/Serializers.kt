package com.fsn.template.application.configuration

import java.math.BigDecimal
import java.time.Instant
import java.util.Currency
import java.util.UUID
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object BigDecimalSerializer : KSerializer<BigDecimal> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)

  override fun deserialize(decoder: Decoder): BigDecimal = decoder.decodeString().toBigDecimal()

  override fun serialize(encoder: Encoder, value: BigDecimal) =
    encoder.encodeString(value.toPlainString())
}

object CurrencySerializer : KSerializer<Currency> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("Currency", PrimitiveKind.STRING)

  override fun deserialize(decoder: Decoder): Currency =
    Currency.getInstance(decoder.decodeString())

  override fun serialize(encoder: Encoder, value: Currency) =
    encoder.encodeString(value.currencyCode)
}

object UuidSerializer : KSerializer<UUID> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

  override fun deserialize(decoder: Decoder): UUID = UUID.fromString(decoder.decodeString())

  override fun serialize(encoder: Encoder, value: UUID) = encoder.encodeString(value.toString())
}

object InstantSerializer : KSerializer<Instant> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

  override fun deserialize(decoder: Decoder): Instant = Instant.parse(decoder.decodeString())

  override fun serialize(encoder: Encoder, value: Instant) = encoder.encodeString(value.toString())
}
