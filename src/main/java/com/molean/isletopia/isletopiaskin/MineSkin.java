package com.molean.isletopia.isletopiaskin;

public class MineSkin {
    public static class Data{
        private Texture texture;
        public static class Texture{
            private String value;
            private String signature;
            public String getValue() {
                return value;
            }
            public void setValue(String value) {
                this.value = value;
            }
            public String getSignature() {
                return signature;
            }
            public void setSignature(String signature) {
                this.signature = signature;
            }
            @Override
            public String toString() {
                return "Texture{" +
                        "value='" + value + '\'' +
                        ", signature='" + signature + '\'' +
                        '}';
            }
        }
        public Texture getTexture() {
            return texture;
        }
        public void setTexture(Texture texture) {
            this.texture = texture;
        }
        @Override
        public String toString() {
            return "Data{" +
                    "texture=" + texture +
                    '}';
        }
    }
    private Data data;
    public Data getData() {
        return data;
    }
    public void setData(Data data) {
        this.data = data;
    }
    @Override
    public String toString() {
        return "MineSkin{" +
                "data=" + data +
                '}';
    }
}
