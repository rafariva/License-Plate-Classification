(ns plate_recognition.getdata	
	(:require
		[clojure.string :as string]
		[clojure.java.io :as io]
		[clj-http.client :as client]
	)
)
(defmacro while-let [[sym expr] & body]
  `(loop [~sym ~expr]
		(when ~sym ~@body
			(recur ~expr)
		)
	)
)
	  
(defn create-folder [folders]
	(doseq [folder folders]
		(println "Creating" folder "folder")
		(.mkdir (java.io.File. (str "resources/" folder)))
	)
)

(defn get-files [path destiny plates]
	(doseq [plate plates]
		(.mkdir (java.io.File. (str destiny plate)))
		(let [stream (->
                (client/get (str path plate ".zip") {:as :byte-array})
                (:body)
                (io/input-stream)
                (java.util.zip.ZipInputStream.))]
			(while-let [entry (.getNextEntry stream)]
				(def filename (last (string/split (.getName entry) #"/")))
				(println "Retrieving" filename "to" destiny)
				(clojure.java.io/copy stream (clojure.java.io/file (str destiny plate "/") filename))
			)
		)
	)
)

(defn -main[& args]
	(create-folder ["training" "testing"])
	(get-files "http://www.rafariva.com/lenguajes_placas/te_" "resources/testing/" ["GSJ2629" "GSK2650" "GSU1495"])
	(get-files "http://www.rafariva.com/lenguajes_placas/tr_" "resources/training/" ["GSJ2629" "GSK2650" "GSU1495"])
)

