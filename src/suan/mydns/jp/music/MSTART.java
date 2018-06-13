package suan.mydns.jp.music;

import java.util.Timer;
import java.util.TimerTask;

import suan.mydns.jp.Thoone;

public class MSTART
{
	public boolean start = false;
	private boolean starts = false;
	private long starttime = 0;
	private long time = 0;
	public int[] nowNotes = {0, 0, 0, 0};
	private Thoone th;
	private boolean pressed = false;
	private boolean autoMove = false;
	private int startBar = 0;
	private int[] startNotes = {0, 0, 0, 0};
	private boolean startBarSet = false;
	private String bar = "0";
	private boolean keyPressed = false;
	private long startBarTime = 0;

	public void Play(Thoone th)
	{
		if(this.start)
		{
			if(!this.starts)
			{
				this.starts = true;
				this.starttime = System.currentTimeMillis() - this.startBarTime;
				for(int i = 0; i < 4; i++)
				{
					this.nowNotes[i] = this.startNotes[i];
				}
				this.th = th;

				Timer T2 = new Timer();
				T2.scheduleAtFixedRate(new AudioTask(), 0, 1000 / (th.mm2.HzMu / th.mm2.onecool));
			}

			if(this.autoMove)
			{
				if(time/((60.0f / (float)th.SPT[th.ch.GetChannel()].Tempo) * th.mm2.HzMu * 4) * (1280 / 6) + 1280/6 + 60 + th.SPT[th.ch.GetChannel()].ShiftX < 1280/6 + 60)
				{
					th.SPT[th.ch.GetChannel()].ShiftX += 1280/6*4;
					if(th.SPT[th.ch.GetChannel()].ShiftX > 0) th.SPT[th.ch.GetChannel()].ShiftX = 0;
				}
				else if(time/((60.0f / (float)th.SPT[th.ch.GetChannel()].Tempo) * th.mm2.HzMu * 4) * (1280 / 6) + 1280/6 + 60 + th.SPT[th.ch.GetChannel()].ShiftX > 1280)
				{
					th.SPT[th.ch.GetChannel()].ShiftX -= 1280/6*4;
				}
			}

			th.rect(time/((60.0f / (float)th.SPT[th.ch.GetChannel()].Tempo) * th.mm2.HzMu * 4) * (1280 / 6) + 1280/6 + 60 + th.SPT[th.ch.GetChannel()].ShiftX, 720/4, 0, 720/4*3);
		}
		else if(this.starts)
		{
			this.starts = false;
		}
	}

	public void Draw(Thoone th)
	{
		th.fill(0xFF);
		if(th.kmState.IsMouseIn(1280/3, 720/8, 1280/12, 720/8))
		{
			th.fill(0xFF, 0x00, 0x00);
			if(th.kmState.MLeft && !this.pressed)
			{
				this.pressed = true;
				this.autoMove = !this.autoMove;
			}
			else if(!th.kmState.MLeft && this.pressed)
			{
				this.pressed = false;
			}
		}
		else if(this.autoMove)
		{
			th.fill(0x00, 0xFF, 0x00);
		}
		th.rect(1280/3, 720/8, 1280/12, 720/8);
		th.fill(0);
		th.textSize(20);
		th.text("Auto\nScroll", 1280/3+10, 720/8+25);

		th.fill(0xFF);
		if(th.kmState.IsMouseIn(1280/3 + 1280 / 12, 720/8, 1280/12, 720/8))
		{
			th.fill(0xFF, 0x00, 0x00);
			if(th.kmState.MLeft && !this.startBarSet)
			{
				this.bar = "";
				this.startBarSet = true;
			}
			else if(this.startBarSet)
			{
				if(!this.keyPressed && th.keyPressed)
				{
					try
					{
						Integer.parseInt(th.key + "");
						this.bar += th.key;
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
			if(this.bar != "") this.startBar = Integer.parseInt(this.bar);
			this.bar = this.startBar + "";
			this.startBarTime = (long)(this.startBar*(60.0 / th.SPT[th.ch.GetChannel()].Tempo * 4) * 1000);

			if(this.startBarSet)
			{
				for(int i = 0; i < 4; i++)
				{
					this.startNotes[i] = 0;
					while(true)
					{
						if(startNotes[i] < th.SPT[i].Volume.size())
						{
							if(th.SPT[i].Time.get(startNotes[i]) <= startBarTime/1000.0*th.mm2.HzMu)
							{
								if(startBarTime/1000.0*th.mm2.HzMu >= th.SPT[i].Time.get(startNotes[i])+th.SPT[i].SoundT.get(startNotes[i]))
								{
									startNotes[i]++;
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

			this.startBarSet = false;
		}
		if(this.startBarSet)
		{
			th.fill(0x00, 0xFF, 0x00);
		}
		th.rect(1280/3 + 1280 / 12, 720/8, 1280/12, 720/8);
		th.fill(0);
		th.textSize(20);
		th.text(this.bar + " Bar\nStart", 1280/3+1280/12+10, 720/8+25);
	}

	class AudioTask extends TimerTask
	{
		@Override
		public void run()
		{
			// TODO 自動生成されたメソッド・スタブ
			if(start)
			{
				boolean b = false;
				time = (long) (((System.currentTimeMillis() - starttime)/1000.0)*th.mm2.HzMu);
				for(int i = 0; i < 4; i++)
				{
					if(nowNotes[i] < th.SPT[i].Volume.size())
					{
						b = true;
						if(th.SPT[i].Time.get(nowNotes[i]) <= time)
						{
							if(th.ch.canPlay[i]) th.mm2.ChStat(th.SPT[i].Freque.get(nowNotes[i]), th.SPT[i].Duty.get(nowNotes[i]), th.SPT[i].Volume.get(nowNotes[i]), th.SPT[i].Voldow.get(nowNotes[i]), th.SPT[i].Fredow.get(nowNotes[i]), true, nowNotes[i], i, th.SPT[i].VolDUM.get(nowNotes[i]));
							else th.mm2.ChStat(i == 3 ? 0 : -1, th.SPT[i].Duty.get(nowNotes[i]), th.SPT[i].Volume.get(nowNotes[i]), th.SPT[i].Voldow.get(nowNotes[i]), th.SPT[i].Fredow.get(nowNotes[i]), true, nowNotes[i], i, th.SPT[i].VolDUM.get(i));
							if(time >= th.SPT[i].Time.get(nowNotes[i])+th.SPT[i].SoundT.get(nowNotes[i]))
							{
								nowNotes[i]++;
							}
						}
						else
						{
							th.mm2.ChStat(i == 3 ? 0 : -1, 0.5f, 16, 0.0f, 1.0f, true, 0, i, 16);
						}
					}
					else
					{
						th.mm2.ChStat(i == 3 ? 0 : -1, 0.5f, 16, 0.0f, 1.0f, true, 0, i, 16);
					}
				}
				if(!b)
				{
					if(th.ch.loop)
					{
						starttime = System.currentTimeMillis();
						for(int i = 0; i < 4; i++)
						{
							nowNotes[i] = 0;
						}
					}
					else
					{
						for(int i = 0; i < 4; i++)
						{
							th.mm2.ChStat(i == 3 ? 0 : -1, 0.5f, 16, 0.0f, 1.0f, true, 0, i, 16);
						}
						for(int i = 0; i < 4; i++)
						{
							nowNotes[i] = 0;
						}
						th.mm2.resk = true;
						th.mm2.resktime = System.currentTimeMillis();
						start = false;
						this.cancel();
					}
				}
			}
		}
	}
}
