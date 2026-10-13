package com.coderpage.mine.app.tally.module.edit.record;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/** Encoding for locally saved, ordered category notes. */
public final class CommonNotes {
    private CommonNotes() { }

    public static List<String> parse(String text) {
        LinkedHashSet<String> notes = new LinkedHashSet<>();
        if (text != null) {
            for (String line : text.split("\\r\\n|\\r|\\n")) {
                String note = line.trim();
                if (!note.isEmpty()) {
                    notes.add(note);
                }
            }
        }
        return new ArrayList<>(notes);
    }

    public static String format(List<String> notes) {
        StringBuilder text = new StringBuilder();
        for (String note : notes) {
            if (text.length() > 0) {
                text.append('\n');
            }
            text.append(note);
        }
        return text.toString();
    }

    public static String key(long accountId, int type, String uniqueName) {
        return accountId + ":" + type + ":" + uniqueName;
    }
}
