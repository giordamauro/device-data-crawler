sqlite> .mode csv
sqlite> .headers on 
sqlite> .output {pathToFile.csv}

sqlite>

SELECT m.brand, m.model, m.model_alias, f.category, f.name, f.value 
FROM device_model m INNER Join device_feature f ON m.id = f.device_id 
ORDER BY m.brand, m.model, f.category, f.name;
