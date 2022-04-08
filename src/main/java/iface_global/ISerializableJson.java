package iface_global;

import org.json.JSONObject;

/** Any class that reads/writes Json files should implement this */
public interface ISerializableJson {
    void importJson(JSONObject jsonObject);
    JSONObject exportJson();
}
