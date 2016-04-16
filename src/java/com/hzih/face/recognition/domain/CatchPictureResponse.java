package com.hzih.face.recognition.domain;


import com.hzih.face.recognition.entity.SipXml;
import com.hzih.face.recognition.utils.Configuration;

/**
 * Created by Administrator on 15-7-24.
 */
public class CatchPictureResponse extends SipXml {
    private String catchPic;

    public String getCatchPic() {
        return catchPic;
    }

    public void setCatchPic(String catchPic) {
        this.catchPic = catchPic;
    }

    public CatchPictureResponse xmlToBean(byte[] buff){
        Configuration config = new Configuration(buff);
        CatchPictureResponse catchPicture = config.getCatchPictureResponseResponse();
        return catchPicture;
    }
}
