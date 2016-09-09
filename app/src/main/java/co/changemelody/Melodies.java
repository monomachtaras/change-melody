package co.changemelody;

/**
 * Created by taras on 15.08.16.
 */
public class Melodies {
    private int _id;
    private String _melodyname;
    private String _uriname;

    public Melodies(){

    }
    public Melodies(String melodyname, String uriname ) {
        this._melodyname = melodyname;
        this._uriname = uriname;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_melodyname() { return _melodyname; }

    public String get_uriname() { return _uriname; }

    public void set_melodyname(String _melodyname) { this._melodyname = _melodyname;}

    public void set_uriname(String _uriname) { this._uriname = _uriname;}
}
