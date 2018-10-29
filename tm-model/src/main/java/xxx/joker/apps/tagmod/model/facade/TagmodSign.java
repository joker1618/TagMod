package xxx.joker.apps.tagmod.model.facade;

import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3v2.TAGv2;
import xxx.joker.apps.tagmod.model.id3v2.TAGv2Builder;
import xxx.joker.apps.tagmod.model.id3v2.frame.ID3v2Frame;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.Comments;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.UserTextInfo;
import xxx.joker.apps.tagmod.model.id3v2.frame.enums.FrameName;
import xxx.joker.apps.tagmod.model.mp3.MP3File;
import xxx.joker.libs.javalibs.language.JkLanguage;
import xxx.joker.libs.javalibs.utils.JkBytes;
import xxx.joker.libs.javalibs.utils.JkEncryption;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
 *
 * This class model the c. TAGv2
 */
public class TagmodSign {

    private static final String ID3_TAGMOD_COMMENT = "ID3 tags create with TagMod";

    private static final String MD5_HASH_DESCR = "MD5 HASH";
    private static final String TAG_CREATION_DESCR = "TAG CREATION TIME";

    private static final int VERSION = 4;

    private String md5hash;
    private LocalDateTime creationTime;

    private TagmodSign() {}

    public static TagmodSign parse(TAGv2 tagv2) {
        if(tagv2.getVersion() != VERSION)   return null;
        if(tagv2.getFrameList().size() != 2)    return null;

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

        return tagmodSign;
    }

    public static TagmodSign create(String md5hash) {
        TagmodSign sign = new TagmodSign();
        sign.md5hash = md5hash;
        sign.creationTime = LocalDateTime.now();
        return sign;
    }

    public byte[] toTAGv2Bytes() {
        TAGv2Builder builder = new TAGv2Builder();
        builder.addFrameData(FrameName.TXXX, new UserTextInfo(MD5_HASH_DESCR, md5hash));
        builder.addFrameData(FrameName.TXXX, new UserTextInfo(TAG_CREATION_DESCR, creationTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        return builder.buildBytes(VERSION, TxtEncoding.ISO_8859_1, true, 0);
    }

    public boolean isValidFor(MP3File mp3File) {
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
}
