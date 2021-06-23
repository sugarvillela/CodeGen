package iface_global;

import codejson.iface.IErrCatch;
import org.json.JSONObject;

public interface ISerializableJson {
    void fromJson(JSONObject jsonObject, IErrCatch errCatch);
    JSONObject toJson();
}
