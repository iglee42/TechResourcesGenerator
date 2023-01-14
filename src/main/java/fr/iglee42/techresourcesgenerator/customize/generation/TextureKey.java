package fr.iglee42.techresourcesgenerator.customize.generation;

public class TextureKey {

    private final String key,object;

    public TextureKey(String key, String object) {
        this.key = key;
        this.object = object;
    }

    public String getObject() {
        return object;
    }

    public String toJson(){
        return "        \""+key+"\": \""+object+"\"";
    }
}
