package javafx;

import database.view.ViewExtendedContact;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.applet.Applet;
import java.awt.*;

public class MapItemsApplet extends Applet {
    ObservableList<ViewExtendedContact> viewContactObservableList;

    public MapItemsApplet(ObservableList<ViewExtendedContact> viewContactObservableList) throws HeadlessException {
        this.viewContactObservableList=viewContactObservableList;
    }

    public JSONArray getJavaArray () {
        JSONArray arr = new JSONArray();
        JSONObject tmp;
        try {
            for (ViewExtendedContact aViewContactObservableList : viewContactObservableList) {
                tmp = new JSONObject();
                tmp.put("Id", aViewContactObservableList.getName()); //some public getters inside GraphUser?
                arr.put(tmp);
            }
            return arr;
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return arr;
    }

}
