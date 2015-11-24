(ns gtfs-feed.core
  (:require [flatland.protobuf.core :as proto]))

(def gtfs-feed-mappings
  {:header com.google.transit.realtime.GtfsRealtime$FeedHeader
   :entity com.google.transit.realtime.GtfsRealtime$FeedEntity
   :alert com.google.transit.realtime.GtfsRealtime$Alert
   :active_period com.google.transit.realtime.GtfsRealtime$TimeRange
   :informed_entity com.google.transit.realtime.GtfsRealtime$EntitySelector
   :trip com.google.transit.realtime.GtfsRealtime$TripDescriptor
   ;;:cause com.google.transit.realtime.GtfsRealtime$Alert$Cause
   ;;:effect com.google.transit.realtime.GtfsRealtime$Alert$Effect
   :url com.google.transit.realtime.GtfsRealtime$TranslatedString
   :header_text com.google.transit.realtime.GtfsRealtime$TranslatedString
   :description_text com.google.transit.realtime.GtfsRealtime$TranslatedString})

(def protodefs
  (into {}
        (map (fn [[k v]]
               [k (proto/protodef v)])
             gtfs-feed-mappings)))

(declare ->gtfs)

(defn kv->gtfs [[k v :as x]]
  (cond (sequential? v) [k (doall (map ->gtfs v))]
        (contains? protodefs k) (let [d (protodefs k)
                                      vs (doall (mapcat kv->gtfs v))]
                                  (println "applying" k vs)
                                  [k (apply proto/protobuf d vs)])
        :else [k v]))

(defn ->gtfs [x]
  (if (map? x) (doall (into {} (map kv->gtfs x)))
      x))

(def FeedMessage (proto/protodef com.google.transit.realtime.GtfsRealtime$FeedMessage))

(defn ->gtfs-feed [x]
  (let [args (mapcat identity (->gtfs x))]
    (println "args" args)
    (apply proto/protobuf FeedMessage args)))


