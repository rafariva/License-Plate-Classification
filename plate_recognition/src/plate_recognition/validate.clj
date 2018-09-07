(ns plate_recognition.validate
	 (:require
	  [clojure.java.io :as io]
	  [cortex.util :as util]
	  [cortex.nn.execute :as execute]
	  [mikera.image.core :as i]
	  [mikera.image.filters :as filters]
	  [think.image.patch :as patch]
	  )
)

(defn get-file [image]
	{:labels ["test"]
	:data (patch/image->patch(-> (i/load-image image) ((filters/grayscale)) (i/resize 64 64) )
	:datatype :float
	:colorspace :gray)}
)

(defn validate-mappings [nippy path mapping]
	(let[obs (get-file path)  mappings (read-string (slurp mapping))]
	(-> (execute/run nippy [obs])
	first
	:labels
	util/max-index
	mappings))
)

(def get-categories
  (zipmap (range) (map #(.getName %) (filter #(.isDirectory %) (.listFiles (io/file "resources/training"))))))

(defn get-images [path]
	(into [] (filter #(let[fname (clojure.string/lower-case %) ]
	(or (clojure.string/includes? fname ".jpg") (clojure.string/includes? fname ".png") )  )
	(map #(.getPath %) (into [] (.listFiles (io/file path))))))
)

(defn validate [nippy path]
	(let[obs (map #(get-file %) path) ]
	(map #(get-categories (util/max-index (:labels %))) (execute/run nippy (into-array obs))))
)

(defn -main[& args]
	(let[ nippy (util/read-nippy-file "trained-network.nippy")
          path (io/as-file "validate/")
          mapping "resources-mapping.edn"
        ]
    (clojure.pprint/pprint
		(if (.isDirectory path)
			(let[imgs (get-images path)] (zipmap imgs (validate nippy imgs))) [path (validate-mappings nippy path mapping)])
		)
	)
)

