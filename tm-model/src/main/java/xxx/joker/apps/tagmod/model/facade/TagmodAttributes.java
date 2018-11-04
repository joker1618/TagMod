package xxx.joker.apps.tagmod.model.facade;

import org.apache.commons.lang3.StringUtils;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.IFrameData;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.TextInfo;
import xxx.joker.apps.tagmod.model.mp3.MP3Attribute;
import xxx.joker.libs.core.utils.JkStreams;

import java.util.*;

public class TagmodAttributes {

    private TreeMap<MP3Attribute, List<IFrameData>> dataMap;

    public TagmodAttributes() {
        this.dataMap = new TreeMap<>(Comparator.comparing(MP3Attribute::getPosition));
    }

    public void addAllAttributes(TagmodAttributes other) {
        other.dataMap.forEach(
                (k,v) -> v.forEach(val -> addAttribute(k, val))
        );
    }

    public boolean addAttribute(MP3Attribute attrib, IFrameData frameData) {
        if(frameData != null) {
            dataMap.putIfAbsent(attrib, new ArrayList<>());
            List<IFrameData> flist = dataMap.get(attrib);
            if (attrib.isMultiValue() || flist.isEmpty()) {
                flist.add(frameData);
                return true;
            }
        }
        return false;
    }

    protected boolean addAttributeString(MP3Attribute attrib, String value) {
        if(StringUtils.isNotBlank(value)) {
            return addAttribute(attrib, new TextInfo(value));
        }
        return false;
    }

    public void setAttribute(MP3Attribute attrib, IFrameData frameData) {
        if(frameData != null) {
            dataMap.putIfAbsent(attrib, new ArrayList<>());
            List<IFrameData> flist = dataMap.get(attrib);
            flist.clear();
            flist.add(frameData);
        }
    }

    public void setAttributeString(MP3Attribute attrib, String value) {
        if(StringUtils.isNotBlank(value)) {
            setAttribute(attrib, new TextInfo(value));
        }
    }

    public void removeAttribute(MP3Attribute attrib) {
        dataMap.remove(attrib);
    }

    public List<IFrameData> getFramesData(MP3Attribute attribute) {
        List<IFrameData> flist = dataMap.get(attribute);
        return flist == null ? Collections.emptyList() : flist;
    }

    public <T extends IFrameData> List<T> getFramesDataCasted(MP3Attribute attribute) {
        List<IFrameData> flist = dataMap.get(attribute);
        return flist == null ? Collections.emptyList() : JkStreams.map(flist, fd -> (T)fd);
    }

    public IFrameData getFrameData(MP3Attribute attribute) {
        if(attribute.isMultiValue() || !dataMap.containsKey(attribute)) {
            return null;
        }
        List<IFrameData> flist = dataMap.get(attribute);
        return flist.size() == 1 ? flist.get(0) : null;

    }

    public <T extends IFrameData> T getFrameDataCasted(MP3Attribute attribute) {
        if(attribute.isMultiValue() || !dataMap.containsKey(attribute)) {
            return null;
        }
        List<IFrameData> flist = dataMap.get(attribute);
        return flist.size() == 1 ? (T) flist.get(0) : null;

    }

    public Map<MP3Attribute, List<IFrameData>> getAttributesDataMap() {
        return dataMap;
    }


}
