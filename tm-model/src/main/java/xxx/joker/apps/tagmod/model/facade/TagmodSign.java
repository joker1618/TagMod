package xxx.joker.apps.tagmod.model.facade;

import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3v2.TAGv2;
import xxx.joker.apps.tagmod.model.id3v2.TAGv2Builder;
import xxx.joker.apps.tagmod.model.id3v2.frame.ID3v2Frame;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.Comments;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.UserTextInfo;
import xxx.joker.apps.tagmod.model.id3v2.frame.enums.FrameName;
import xxx.joker.apps.tagmod.model.mp3.MP3File;
import xxx.joker.libs.language.JkLanguage;
import xxx.joker.libs.core.utils.JkBytes;
import xxx.joker.libs.core.utils.JkConverter;
import xxx.joker.libs.core.utils.JkEncryption;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * TagMod Signature structure
 *
 * 1. exactly 2 ID3v2 tags, [attributes, signature]
 * 2. no dirty bytes
 * 3. ID3v1 must exists
 * 4. file size equals to the sum of previous
 *
 * a. 1 frame comment in the first ID3v2 tag = ID3_TAGMOD_COMMENT
 * b. the field comment in ID3v1 tag = ID3_TAGMOD_COMMENT
 * c. an ID3v2.4 tag, with the following TXXX frames:
 *      - MD5 HASH: md5 hash of [first ID3v2 tag + ID3v1 tag)
 *      - TAG CREATION: ISO datetime
 *      - TAG SIGN_TAG_VERSION: [2,3,4]
 *      - TAG ENCODING: [iso,utf16,utf16be,utf8]
 *
 * This class model the c. TAGv2
 */
public class TagmodSign {

    private static final String ID3_TAGMOD_COMMENT = "ID3 tags create with TagMod";

    private static final String MD5_HASH_DESCR = "MD5 HASH";
    private static final String TAG_CREATION_DESCR = "TAG CREATION TIME";
    private static final String TAG_VERSION_DESCR = "TAG VERSION";
    private static final String TAG_ENCONDING_DESCR = "TAG ENCODING";

    private static final int SIGN_TAG_VERSION = 4;

    private boolean valid;
    private Integer tagNum;
    private String md5hash;
    private LocalDateTime creationTime;
    private Integer version;
    private TxtEncoding encoding;

    private TagmodSign() {}

    public static TagmodSign parse(MP3File mp3File) {
        List<TAGv2> flist = mp3File.getTAGv2List();
        TagmodSign sign = null;
        int i;
        for(i = 0; i < flist.size() && sign == null; i++) {
            sign = parse(flist.get(i));
        }

        if(sign == null)    return null;

        sign.tagNum = i;
        sign.valid = checkValidity(mp3File, sign.md5hash);

        return sign;
    }
    private static TagmodSign parse(TAGv2 tagv2) {
        if(tagv2.getVersion() != SIGN_TAG_VERSION)   return null;
        if(tagv2.getFrameList().size() != 4)    return null;

        TagmodSign tagmodSign = new TagmodSign();

        ID3v2Frame frame = tagv2.getFrameList().get(0);
        if(frame.getFrameName() != FrameName.TXXX)  return null;
        UserTextInfo tinfo = frame.getFrameData();
        if(!MD5_HASH_DESCR.equals(tinfo.getDescription()))   return null;
        tagmodSign.md5hash = tinfo.getInfo();

        frame = tagv2.getFrameList().get(1);
        if(frame.getFrameName() != FrameName.TXXX)  return null;
        tinfo = frame.getFrameData();
        if(!TAG_CREATION_DESCR.equals(tinfo.getDescription()))   return null;
        tagmodSign.creationTime = parseTagCreation(tinfo.getInfo());
        if(tagmodSign.creationTime == null)     return null;

        frame = tagv2.getFrameList().get(2);
        if(frame.getFrameName() != FrameName.TXXX)  return null;
        tinfo = frame.getFrameData();
        if(!TAG_VERSION_DESCR.equals(tinfo.getDescription()))   return null;
        tagmodSign.version = JkConverter.stringToInteger(tinfo.getInfo());
        if(tagmodSign.version == null)     return null;

        frame = tagv2.getFrameList().get(3);
        if(frame.getFrameName() != FrameName.TXXX)  return null;
        tinfo = frame.getFrameData();
        if(!TAG_ENCONDING_DESCR.equals(tinfo.getDescription()))   return null;
        tagmodSign.encoding = TxtEncoding.fromLabel(tinfo.getInfo());
        if(tagmodSign.encoding == null)     return null;

        return tagmodSign;
    }

    private static boolean checkValidity(MP3File mp3File, String md5hash) {
        if(mp3File.getTAGv2List().size() != 2 || mp3File.getTAGv1() == null) {
            return false;
        }

        byte[] t1bytes = mp3File.getTAGv1().toBytes();
        byte[] t2bytes = TAGv2Builder.toBytes(mp3File.getTAGv2List().get(0));
        byte[] bytes = JkBytes.mergeArrays(t2bytes, t1bytes);
        String md5 = JkEncryption.getMD5(bytes);
        if(!md5.equals(md5hash))       return false;

        int signLen = mp3File.getTAGv2List().get(1).getTagLength();
        long songLen = mp3File.getSongDataFPos().getLength();

        long sum = t2bytes.length + signLen + songLen + t1bytes.length;

        return mp3File.getFileSize() == sum;
    }

    public static TagmodSign create(String md5hash, int version, TxtEncoding enc) {
        TagmodSign sign = new TagmodSign();
        sign.valid = true;
        sign.md5hash = md5hash;
        sign.creationTime = LocalDateTime.now();
        sign.version = version;
        sign.encoding = enc;
        return sign;
    }

    public byte[] toTAGv2Bytes() {
        TAGv2Builder builder = new TAGv2Builder();
        builder.addFrameData(FrameName.TXXX, new UserTextInfo(MD5_HASH_DESCR, md5hash));
        builder.addFrameData(FrameName.TXXX, new UserTextInfo(TAG_CREATION_DESCR, creationTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        if(isValid()) {
            builder.addFrameData(FrameName.TXXX, new UserTextInfo(TAG_VERSION_DESCR, version + ""));
            builder.addFrameData(FrameName.TXXX, new UserTextInfo(TAG_ENCONDING_DESCR, encoding.getLabel()));
        }
        return builder.buildBytes(SIGN_TAG_VERSION, TxtEncoding.UTF_16, true, null);
    }

    private static LocalDateTime parseTagCreation(String str) {
        try {
            return LocalDateTime.parse(str, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch(Exception ex) {
            return null;
        }
    }

    public static Comments getID3v2FrameComment() {
        return new Comments(JkLanguage.ENGLISH, "", getID3TagmodComment());
    }

    public static String getID3TagmodComment() {
        return ID3_TAGMOD_COMMENT;
    }

    public String getMd5hash() {
        return md5hash;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public boolean isValid() {
        return valid;
    }

    public Integer getVersion() {
        return version;
    }

    public TxtEncoding getEncoding() {
        return encoding;
    }

    public Integer getTagNum() {
        return tagNum;
    }
}
