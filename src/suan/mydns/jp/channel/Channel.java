package suan.mydns.jp.channel;

import suan.mydns.jp.Thoone;

public class Channel
{
	private String[] Text = {"1Ch\nSquare", "2Ch\nSquare", "3Ch\nTriangle", "4Ch\nNoise", "5Ch\nDPCM"};
	private int channel = 0;
	public boolean loop = false;
	private boolean pressed = false;
	public int loopstartnum = 0;
	public String loopstring = "0";
	private boolean loopstart = false;
	private boolean keyPressed = false;
	public boolean[] canPlay = {true, true, true, true, true};

	public void SetChannel(int Ch)
	{
		this.channel = Ch;
	}

	public int GetChannel()
	{
		return this.channel;
	}

	public void Draw(Thoone th)
	{
		for(int i = 0; i < 5; i++)
		{
			th.color(0);
			th.fill(255);
			if(this.canPlay[i]) th.fill(0x00, 0xFF, 0x00);
			if(this.channel == i) th.fill(0xFF, 0x00, 0x00);
			th.rect(0, 720/5*i, 1280/6, 720/5);
			th.fill(0);
			th.textSize(50);
			th.text(Text[i], 10, 720/5*i+300/5);
			if(th.kmState.MLeft)
			{
				if(th.kmState.IsMouseIn(0, 720/5*i, 1280/6, 720/5))
				{
					if(i < 3) th.mm2.ChStat(th.mm2.Sn[4][th.mm2.C], 0.5f, 16, 0, 1, false, 1, i, 16);
					else th.mm2.ChStat(5, 0.0f, 16, 0, 1, false, 1, i, 16);
					channel = i;
					this.canPlay[i] = true;
				}
			}
			else if(th.kmState.IsMouseIn(0, 720/5*i, 1280/6, 720/5) && !th.musics.start)
			{
				if(i < 3 || i == 4) th.mm2.ChStat(-1, 0.5f, 16, 0, 1, false, -1, i, 16);
				else th.mm2.ChStat(0, 0.5f, 16, 0, 1, false, -1, i, 16);
			}

			if(th.kmState.IsMouseIn(0, 720/5*i, 1280/6, 720/5) && th.kmState.MRight && !this.pressed)
			{
				this.pressed = true;
				this.canPlay[i] = !this.canPlay[i];
			}
			else if(th.kmState.IsMouseIn(0, 720/5*i, 1280/6, 720/5) && !th.kmState.MRight && this.pressed)
			{
				this.pressed = false;
			}
		}

		th.fill(0xFF);
		if(this.loop)
		{
			th.fill(0x00, 0xFF, 0x00);
		}
		if(th.kmState.IsMouseIn(1280/2, 0, 1280/12, 720/24))
		{
			th.fill(0xFF, 0x00, 0x00);
			if(th.kmState.MLeft && !this.pressed)
			{
				this.pressed = true;
				this.loop = !this.loop;
			}
			else if(!th.kmState.MRight && !th.kmState.MLeft && this.pressed)
			{
				this.pressed = false;
			}
		}
		th.rect(1280/2, 0, 1280/12, 720/24);
		th.fill(0);
		th.textSize(20);
		th.text("Loop", 1280/2+10, 720/48+10);

		th.fill(0xFF);
		if(th.kmState.IsMouseIn(1280/2, 720/24, 1280/12, 720/12))
		{
			th.fill(0xFF, 0x00, 0x00);
			if(th.kmState.MLeft && !this.loopstart)
			{
				this.loopstring = "";
				this.loopstart = true;
			}
			else if(this.loopstart)
			{
				if(!this.keyPressed && th.keyPressed)
				{
					try
					{
						Integer.parseInt(th.key + "");
						this.loopstring += th.key;
					}
					catch(Exception e)
					{

					}
					this.keyPressed = true;
				}
				else if(this.keyPressed && !th.keyPressed)
				{
					this.keyPressed = false;
				}
			}
		}
		else
		{
			if(this.loopstring != "") this.loopstartnum = Integer.parseInt(this.loopstring);
			this.loopstring = this.loopstartnum + "";
			th.musics.loopBarTime = (long)(this.loopstartnum*(60.0 / th.SPT[th.ch.GetChannel()].Tempo * 4) * 1000);

			if(this.loopstart)
			{
				for(int i = 0; i < 5; i++)
				{
					th.musics.loopNotes[i] = 0;
					while(true)
					{
						if(th.musics.loopNotes[i] < th.SPT[i].Volume.size())
						{
							if(th.SPT[i].Time.get(th.musics.loopNotes[i]) <= th.musics.loopBarTime/1000.0*th.mm2.HzMu)
							{
								if(th.musics.loopBarTime/1000.0*th.mm2.HzMu >= th.SPT[i].Time.get(th.musics.loopNotes[i])+th.SPT[i].SoundT.get(th.musics.loopNotes[i]))
								{
									th.musics.loopNotes[i]++;
								}
								else
								{
									break;
								}
							}
							else
							{
								break;
							}
						}
						else
						{
							break;
						}
					}
				}
			}

			this.loopstart = false;
		}
		if(this.loopstart)
		{
			th.fill(0x00, 0xFF, 0x00);
		}
		th.rect(1280/2, 720/24, 1280/12, 720/12);
		th.fill(0);
		th.textSize(20);
		th.text(this.loopstring + " Bar\nLoop", 1280/2+10, 720/16+10);
	}
}
