package org.acme.restclient

class Country {

    lateinit var name: String
    lateinit var alpha2Code: String
    lateinit var capital: String
    lateinit var currencies: List<Currency>

    class Currency {
        lateinit var code: String
        lateinit var name: String
        lateinit var symbol: String
    }

}
