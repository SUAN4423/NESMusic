package suan.mydns.jp.state;

import java.util.ArrayList;

import suan.mydns.jp.Thoone;
import suan.mydns.jp.track.SuperTrack;

public class Move
{
	public boolean enableMove = false;
	private boolean pressed = false;
	private boolean first = false;
	private int[] position = {0, 0};
	private long[] x = {0, 0};
	private byte shori = 0;
	private long[] ss = {0, 0};
	private ArrayList<Integer> copy = new ArrayList<>();
	private boolean keyPress = false;

	public void move(Thoone th, SuperTrack st)
	{
		long b = 0;
		if(th.kmState.IsMouseIn(1280/6, 720/4, 1280/6*5, 720/4*3))
		{
			int a = (th.kmState.Mouse[0]-1280/6-60-st.ShiftX);
			int k = -st.ShiftX/(1280/6)*st.Nag;
			while(true)
			{
				if(k*(1280/6)/st.Nag <= a && (k+1)*(1280/6)/st.Nag > a)
				{
					break;
				}
				k++;
			}
			b = (long) ((k)*((60.0 / st.Tempo) * th.mm2.HzMu * 4 / st.Nag));
		}

		if(!this.first && th.kmState.IsMouseIn((int)(x[0]/((60.0f / (float)st.Tempo) * th.mm2.HzMu * 4) * (1280 / 6) + 1280/6 + 60 + st.ShiftX), 720/4, (int)((x[1] - x[0])/((60.0f / (float)st.Tempo) * th.mm2.HzMu * 4) * (1280 / 6)), 720/4*3))
		{
		}
		else
		{
			if(th.kmState.MLeft)
			{

				if(!this.first)
				{
					this.first = true;
					this.position[0] = th.kmState.Mouse[0];
					this.position[1] = th.kmState.Mouse[1];
					x[0] = b;
				}
				x[1] = b;
			}
			else
			{
				this.first = false;
			}
		}

		th.fill(0xFF, 0x00, 0x00, 0x90);
		th.rect(x[0]/((60.0f / (float)st.Tempo) * th.mm2.HzMu * 4) * (1280 / 6) + 1280/6 + 60 + st.ShiftX, 720/4, (x[1] - x[0])/((60.0f / (float)st.Tempo) * th.mm2.HzMu * 4) * (1280 / 6), 720/4*3);

		if(th.kmState.Key.get(th.CONTROL))
		{
			if(th.kmState.Key.get(67) && !this.keyPress)
			{
				this.copy.clear();
				this.keyPress = true;
				this.shori = 1;
				this.ss[0] = this.x[0];
				this.ss[1] = this.x[1];
				for(int i = 0; i < th.SPT[th.ch.GetChannel()].Time.size(); i++)
				{
					if(Math.min(this.ss[0], this.ss[1]) <= th.SPT[th.ch.GetChannel()].Time.get(i) && Math.max(this.ss[0], this.ss[1]) > th.SPT[th.ch.GetChannel()].Time.get(i)) this.copy.add(i);
				}
			}
			if(th.kmState.Key.get(88) && !this.keyPress)
			{
				this.copy.clear();
				this.keyPress = true;
				this.shori = 2;
				this.ss[0] = this.x[0];
				this.ss[1] = this.x[1];
				for(int i = 0; i < th.SPT[th.ch.GetChannel()].Time.size(); i++)
				{
					if(Math.min(this.ss[0], this.ss[1]) <= th.SPT[th.ch.GetChannel()].Time.get(i) && Math.max(this.ss[0], this.ss[1]) > th.SPT[th.ch.GetChannel()].Time.get(i)) this.copy.add(i);
				}
			}
			if(th.kmState.Key.get(86) && !this.keyPress)
			{
				this.keyPress = true;
				long sho = this.x[0] - Math.min(this.ss[0], this.ss[1]);
				if(this.shori == 1)
				{
					for(int i = 0; i < this.copy.size(); i++)
					{
						th.SPT[th.ch.GetChannel()].Volume.add(th.SPT[th.ch.GetChannel()].Volume.get(this.copy.get(i)));
						th.SPT[th.ch.GetChannel()].Freque.add(th.SPT[th.ch.GetChannel()].Freque.get(this.copy.get(i)));
						th.SPT[th.ch.GetChannel()].FrequI.add(th.SPT[th.ch.GetChannel()].FrequI.get(this.copy.get(i)));
						th.SPT[th.ch.GetChannel()].Time.add(th.SPT[th.ch.GetChannel()].Time.get(this.copy.get(i)) + sho);
						th.SPT[th.ch.GetChannel()].SoundT.add(th.SPT[th.ch.GetChannel()].SoundT.get(this.copy.get(i)));
						th.SPT[th.ch.GetChannel()].Duty.add(th.SPT[th.ch.GetChannel()].Duty.get(this.copy.get(i)));
						th.SPT[th.ch.GetChannel()].Voldow.add(th.SPT[th.ch.GetChannel()].Voldow.get(this.copy.get(i)));
						th.SPT[th.ch.GetChannel()].Fredow.add(th.SPT[th.ch.GetChannel()].Fredow.get(this.copy.get(i)));
					}
				}
				else if(this.shori == 2)
				{
					for(int i = 0; i < this.copy.size(); i++)
					{
						th.SPT[th.ch.GetChannel()].Time.set(this.copy.get(i), th.SPT[th.ch.GetChannel()].Time.get(this.copy.get(i)) + sho);
					}
				}
			}
			if(!th.kmState.Key.get(67) && !th.kmState.Key.get(88) && !th.kmState.Key.get(86) && !th.kmState.Key.get(127) && !this.keyPress)
			{
				this.keyPress = false;
			}
		}
		else if(th.kmState.Key.get(127) && !this.keyPress)
		{
			this.copy.clear();
			this.keyPress = true;
			this.shori = 2;
			this.ss[0] = this.x[0];
			this.ss[1] = this.x[1];
			for(int i = 0; i < th.SPT[th.ch.GetChannel()].Time.size(); i++)
			{
				if(Math.min(this.ss[0], this.ss[1]) <= th.SPT[th.ch.GetChannel()].Time.get(i) && Math.max(this.ss[0], this.ss[1]) > th.SPT[th.ch.GetChannel()].Time.get(i)) this.copy.add(i);
			}
			for(int i = 0; i < this.copy.size(); i++)
			{
				th.SPT[th.ch.GetChannel()].Time.set(this.copy.get(i), -1l);
			}
			for(int i = 0; i < th.SPT[th.ch.GetChannel()].Time.size(); i++)
			{
				if(th.SPT[th.ch.GetChannel()].Time.get(i) == -1)
				{
					th.SPT[th.ch.GetChannel()].Volume.remove(i);
					th.SPT[th.ch.GetChannel()].Freque.remove(i);
					th.SPT[th.ch.GetChannel()].FrequI.remove(i);
					th.SPT[th.ch.GetChannel()].Time.remove(i);
					th.SPT[th.ch.GetChannel()].SoundT.remove(i);
					th.SPT[th.ch.GetChannel()].Duty.remove(i);
					th.SPT[th.ch.GetChannel()].Voldow.remove(i);
					th.SPT[th.ch.GetChannel()].Fredow.remove(i);
					i--;
				}
			}
		}
		else if(!th.kmState.Key.get(th.CONTROL) && !th.kmState.Key.get(127) && this.keyPress)
		{
			this.keyPress = false;
		}
	}

	public void Draw(Thoone th)
	{
		th.fill(0xFF);
		if(th.kmState.IsMouseIn((int)(1280/6*4.5f), 0, 1280/12, 720/8))
		{
			th.fill(0xFF, 0x00, 0x00);
			if(!this.pressed && th.kmState.MLeft)
			{
				this.pressed = true;
				this.enableMove = !this.enableMove;
			}
			else if(this.pressed && !th.kmState.MLeft)
			{
				this.pressed = false;
			}
		}
		else if(this.enableMove)
		{
			th.fill(0x00, 0xFF, 0x00);
		}
		th.rect(1280/6*4.5f, 0, 1280/12, 720/8);
		th.fill(0);
		th.textSize(20);
		th.text("Select\nmode", 1280/6*4.5f+10, 25);
	}
}
