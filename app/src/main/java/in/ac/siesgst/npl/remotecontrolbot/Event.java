package in.ac.siesgst.npl.remotecontrolbot;

public class Event {
	private String name;
	private int backgroundDrawable;
	private int buttonDrawable;
	private int joyDrawable;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getBackgroundDrawable()
	{
		return backgroundDrawable;
	}

	public void setBackgroundDrawable(int backgroundDrawable)
	{
		this.backgroundDrawable = backgroundDrawable;
	}

	public int getButtonDrawable()
	{
		return buttonDrawable;
	}

	public void setButtonDrawable(int buttonDrawable)
	{
		this.buttonDrawable = buttonDrawable;
	}

	public int getJoyDrawable()
	{
		return joyDrawable;
	}

	public void setJoyDrawable(int joyDrawable)
	{
		this.joyDrawable = joyDrawable;
	}

	public Event(String name, int backgroundDrawable, int buttonDrawable, int joyDrawable) {
		this.name = name;
		this.backgroundDrawable = backgroundDrawable;
		this.buttonDrawable = buttonDrawable;
		this.joyDrawable = joyDrawable;
	}
}
