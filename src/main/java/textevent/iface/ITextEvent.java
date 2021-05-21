package textevent.iface;

import iface_global.ICsv;
import langdef.CMD;
import langdefalgo.iface.LANG_STRUCT;

/** Immutable data object */
public interface ITextEvent extends ICsv {
    LANG_STRUCT langStruct();
    CMD cmd();

    void setEventText(String eventText);
    String getEventText();

    // ICsv
    //String csvString();
    //String friendlyString();
}
