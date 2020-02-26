(use 'clojure.xml)

(def WEATHER-URI "https://query.yahooapis.com/v1/public/yql?q=select * from weather.forecast where woeid=%d&format=xml")

(defn get-location [city-code]
  (for [x (xml-seq (parse (format WEATHER-URI city-code)))
        :when (= :yweather:location (:tag x))]
    (str (:city (:attrs x)) "," (:region (:attrs x)))))

(defn get-temp [city-code]
  (for [x (xml-seq (parse (format WEATHER-URI city-code)))
        :when (= :yweather:condition (:tag x))]
    (:temp (:attrs x))))

(println "weather for " (get-location 12770744) "is " (get-temp 12770744))
