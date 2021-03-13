package ru.apolyakov

case class CrimeInfo(
                      district: String,
                      name: String,
                      crimes_total: Long,
                      crimes_monthly: Long,
                      lat: Double,
                      lng: Double,
                      count: Long)
