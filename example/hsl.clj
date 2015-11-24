(ns gtfs-feed.example.hsl
  (:require [gtfs-feed.core :as gtfs]
            [clj-time.core :as time]
            [clj-time.coerce :as timec]))

(defn fake-response []
  (let [start (timec/to-epoch (time/minus (time/now) (time/hours 1)))
        end (timec/to-epoch (time/plus (time/now) (time/hours 1)))]
  (gtfs/->gtfs-feed {:header {:gtfs_realtime_version "1.0"
                         :timestamp (timec/to-epoch (time/now))}
                :entity {:id "42"
                         :alert {:active_period {:start start :end end}
                                 :informed_entity [{:agency_id "HSL"
                                                    :route_id "1004"
                                                    :route_type 0
                                                    :trip {:routeid "1004"
                                                           :direction_id 1 }}
                                                   {:agency_id "HSL"
                                                    :route_id "1007B"
                                                    :route_type 0
                                                    :trip {:routeid "1007B"
                                                           :direction_id 1 }}]
                                 :description_text {:translation {:text "Raitiolinjat: 4 Katajanokan suuntaan ja 7B, poikkeusreitti."
                                                                  :language "fi"}}}}})))
