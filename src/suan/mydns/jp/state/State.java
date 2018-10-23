package suan.mydns.jp.state;

import processing.core.PApplet;
import suan.mydns.jp.Thoone;
import suan.mydns.jp.music.Wave;

public class State
{
	public static double NortsSize = 1.0;

	public void TempoSet(Thoone th, double Tempo)
	{
		for(int i = 0; i < 4; i++)
		{
			th.SPT[i].Tempo = Tempo;
			th.SPT[i].Param[4] = Tempo + "";
		}
	}

	private boolean pressed = false;
	private boolean sub_pressed = false;
	private String NortsStr = "1.0";
	private boolean keypressed = false;
	private boolean wave = false;
	public static Wave wv;

	public void Draw(Thoone th)
	{
		if(th.kmState.Key.get(114) && !keypressed)
		{
			keypressed = true;
			if(!wave)
			{
				wave = true;
				PApplet.main("suan.mydns.jp.music.Wave");
				Wave.th = th;
			}
			else
			{
				wv.changeVisible();
			}
			if(Wave.Visible) th.kmState.Key.put(114, false);
		}
		else if(!th.kmState.Key.get(114) && keypressed)
		{
			keypressed = false;
		}

		th.fill(255);
		if(th.kmState.IsMouseIn(1280/6*4, 0, 1280/12, 720/8))
		{
			th.fill(0xFF, 0x00, 0x00);
		}
		if(this.pressed && th.kmState.IsMouseIn(1280/6*4, 0, 1280/12, 720/8))
		{
			th.fill(0x00, 0xFF, 0x00);
		}
		th.rect(1280/6*4, 0, 1280/12, 720/8);
		th.fill(0);
		th.textSize(20);
		th.text(this.NortsStr + "\ntime(s)\nnote", 1280/6*4+10, 20);
		if(th.kmState.MLeft && !this.pressed && th.kmState.IsMouseIn(1280/6*4, 0, 1280/12, 720/8))
		{
			this.pressed = true;
			this.NortsStr = "";
		}
		else if(this.pressed && th.kmState.IsMouseIn(1280/6*4, 0, 1280/12, 720/8))
		{
			if(!this.sub_pressed && th.keyPressed)
			{
				if(th.key == '.')
				{
					this.NortsStr += th.key;
				}
				else
				{
					try
					{
						Integer.parseInt(th.key + "");
						this.NortsStr += th.key;
					}
					catch(Exception e)
					{

					}
				}
				this.sub_pressed = true;
			}
			else if(this.sub_pressed && !th.keyPressed)
			{
				this.sub_pressed = false;
			}
		}
		else if(this.pressed)
		{
			if(this.NortsStr != "")
			{
				try
				{
					this.NortsSize = Double.parseDouble(this.NortsStr);
					this.NortsStr = this.NortsSize + "";
				}
				catch(NumberFormatException e)
				{
					e.printStackTrace();
				}
			}
			this.pressed = false;
		}
	}
}
