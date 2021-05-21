package textevent.impl;

import langdef.CMD;
import langdefalgo.iface.LANG_STRUCT;
import textevent.iface.ITextEvent;

import java.util.ArrayList;

import static runstate.Glob.NULL_TEXT;

public class TextEvent implements ITextEvent {
    private static final String FORMAT_CSV = "%s,%s,%s";
    private final LANG_STRUCT langStruct;
    private final CMD cmd;
    private String eventText;

    public TextEvent(LANG_STRUCT langStruct, CMD cmd, String eventText) {
        this.langStruct = langStruct;
        this.cmd = cmd;
        this.eventText = eventText;
    }

    @Override
    public LANG_STRUCT langStruct() {
        return langStruct;
    }

    @Override
    public CMD cmd() {
        return cmd;
    }

    @Override
    public void setEventText(String eventText) {
        this.eventText = eventText;
    }

    @Override
    public String getEventText() {
        return eventText;
    }

    @Override
    public String csvString() {
        return String.format(FORMAT_CSV,
                (cmd == null)?          NULL_TEXT : cmd.toString(),
                (langStruct == null)?   NULL_TEXT : langStruct.toString(),
                (eventText == null)?    NULL_TEXT : eventText
        );
    }

    @Override
    public String friendlyString() {
        ArrayList<String> out = new ArrayList<>();
        if(langStruct != null){ out.add("langStruct: " + langStruct.toString()); }
        if(cmd != null){ out.add("command: " + cmd.toString()); }
        if(eventText != null){ out.add("eventText: " + eventText); }
        return String.join(", ", out);
    }
}
