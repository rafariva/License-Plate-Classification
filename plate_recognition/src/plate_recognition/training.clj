(ns plate_recognition.training
	(:require
		[clojure.java.io :as io]
		[think.image.patch :as patch]
		[mikera.image.core :as imagez]
		[cortex.experiment.util :as experiment-util]
		[cortex.experiment.classification :as classification]
		[cortex.nn.layers :as layers]
	)
)

(defn initial-description-preset [input-w input-h num-classes]
	[(layers/input input-w input-h 1 :id :data)
     (layers/convolutional 3 0 1 5)
     (layers/dropout 0.8)
     (layers/relu)
     (layers/convolutional 3 0 1 3)
	 (layers/dropout 0.9)
     (layers/batch-normalization)
     (layers/linear 1000)
     (layers/relu  :center-loss {:label-indexes {:stream :labels}
                   :label-inverse-counts {:stream :labels}
                   :labels {:stream :labels}
                   :alpha 0.9
                   :lambda 1e-4})
     (layers/dropout 1)
     (layers/linear num-classes)
     (layers/softmax :id :labels)
	]
)

(defn observation->image [image-size observation]
	(patch/patch->image observation image-size))
  
(defn create-mapping [categories]
	{:class-name->index (zipmap categories (range))
	:index->class-name (zipmap (range) categories)}
)
  
(defn training[path training testing]
	(let[
		training-folder (str path training)
		testing-folder	(str path testing)
		categories      (into [] (map #(.getName %) (.listFiles (io/file training-folder))))
		num-classes     (count categories)
		class-mapping	(create-mapping categories)
		image-size      (.getWidth (imagez/load-image (imagez/load-image (first (filter #(.isFile %) (file-seq (io/file training-folder)))))))
		training-ds		(-> training-folder (experiment-util/create-dataset-from-folder class-mapping :image-aug-fn (:image-aug-fn {})) (experiment-util/infinite-class-balanced-dataset))
		testing-ds		(-> testing-folder (experiment-util/create-dataset-from-folder class-mapping))
		initial-description (initial-description-preset image-size image-size num-classes)
		listener 		(classification/create-listener observation->image class-mapping {})
	]
	(spit (str (.getName (io/as-file path)) "-mapping.edn") (zipmap (range) categories))
	(classification/perform-experiment initial-description training-ds testing-ds listener)
    )
)

(defn -main[& args]
    (training "resources/" "training" "testing"))