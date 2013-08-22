package hdlnc.dev2.sangtae.cnm.global.DATA;

public class GuideImage_DATA implements Cloneable {

	private String	GuideImageID = "NULL";
	private String	GroupType = "NULL";
	private String	Subject = "NULL";
	private String	ImageFileURL = "NULL";
	private String	GuideURL = "NULL";
	private String	Content = "NULL";
	private String	RegisterDate = "NULL";
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (GuideImage_DATA)super.clone();
	}

	
	public String getGuideImageID() {
		return GuideImageID;
	}


	public void setGuideImageID(String guideImageID) {
		GuideImageID = guideImageID;
	}


	public String getGroupType() {
		return GroupType;
	}

	public void setGroupType(String groupType) {
		GroupType = groupType;
	}

	public String getSubject() {
		return Subject;
	}

	public void setSubject(String subject) {
		Subject = subject;
	}

	public String getImageFileURL() {
		return ImageFileURL;
	}

	public void setImageFileURL(String imageFileURL) {
		ImageFileURL = imageFileURL;
	}

	public String getGuideURL() {
		return GuideURL;
	}

	public void setGuideURL(String guideURL) {
		GuideURL = guideURL;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getRegisterDate() {
		return RegisterDate;
	}

	public void setRegisterDate(String registerDate) {
		RegisterDate = registerDate;
	}
	
	
	
}

