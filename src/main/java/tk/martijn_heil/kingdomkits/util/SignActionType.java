package tk.martijn_heil.kingdomkits.util;


import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public enum SignActionType
{
    SET_CLASS("SetClass");


    @Getter String text;

    SignActionType(String text)
    {
        this.text = text;
    }

    @Nullable
    public static SignActionType fromString(String s)
    {
        for (SignActionType type : SignActionType.values())
        {
            if(type.getText().equalsIgnoreCase(s)) return type;
        }

        return null;
    }


    public static boolean isValidSignActionType(String s)
    {
        for (SignActionType type : SignActionType.values())
        {
            if(type.getText().equalsIgnoreCase(s)) return true;
        }

        return false;
    }
}
