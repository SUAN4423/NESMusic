package suan.mydns.jp.state;

import suan.mydns.jp.Thoone;

public class Sort
{
	private boolean Pressed = false;
	private long Time = 0;

	public void Draw(Thoone th)
	{
		th.fill(0xFF);
		if(th.kmState.IsMouseIn(1280/6*5, 0, 1280/12, 720/8))
		{
			th.fill(0xFF, 0x00, 0x00);
			if(th.kmState.MLeft && !this.Pressed && !this.isSort)
			{
				this.Pressed = true;
				this.sort(th);
				//this.test(th);
			}
			else if(!th.kmState.MLeft && this.Pressed)
			{
				this.Pressed = false;
			}
		}
		if(this.isSort)
		{
			if(System.currentTimeMillis() % 1000 < 500)
			{
				th.fill(0x00, 0xFF, 0x00);
			}
			else
			{
				th.fill(0x00, 0x00, 0xFF);
			}
		}
		th.rect(1280/6*5, 0, 1280/12, 720/8);
		th.fill(0x00);
		th.textSize(20);
		th.text("Sort", 1280/6*5+10, 25);
		if(System.currentTimeMillis() - this.Time < 1000)
		{
			th.text("Finisied", 1280/6*5+10, 50);
		}
	}

	public void sort(Thoone th)
	{
		this.th = th;
		thr ths = new thr();
		ths.start();
	}

	public boolean isSort = false;

	Thoone th;

	private void test(Thoone th)
	{
		isSort = true;
		for(int i = 0; i < 5; i++)
		{
			for(int j = 0; j < th.SPT[i].Volume.size(); j++)
			{
				int n = 0;
				long m = Long.MAX_VALUE;
				for(int k = j; k < th.SPT[i].Volume.size(); k++)
				{
					if(m > th.SPT[i].Time.get(k))
					{
						n = k;
						m = th.SPT[i].Time.get(k);
					}
				}
				th.SPT[i].Volume.add(j, th.SPT[i].Volume.get(n));
				th.SPT[i].Volume.remove(n+1);
				th.SPT[i].Freque.add(j, th.SPT[i].Freque.get(n));
				th.SPT[i].Freque.remove(n+1);
				th.SPT[i].FrequI.add(j, th.SPT[i].FrequI.get(n));
				th.SPT[i].FrequI.remove(n+1);
				th.SPT[i].Time.add(j, th.SPT[i].Time.get(n));
				th.SPT[i].Time.remove(n+1);
				th.SPT[i].SoundT.add(j, th.SPT[i].SoundT.get(n));
				th.SPT[i].SoundT.remove(n+1);
				th.SPT[i].Duty.add(j, th.SPT[i].Duty.get(n));
				th.SPT[i].Duty.remove(n+1);
				th.SPT[i].Voldow.add(j, th.SPT[i].Voldow.get(n));
				th.SPT[i].Voldow.remove(n+1);
				th.SPT[i].Fredow.add(j, th.SPT[i].Fredow.get(n));
				th.SPT[i].Fredow.remove(n+1);
			}
		}
		Time = System.currentTimeMillis();
		isSort = false;
	}

	class thr extends Thread
	{
		@Override
		public void run()
		{
			isSort = true;
			for(int i = 0; i < 5; i++)
			{
				for(int j = 0; j < th.SPT[i].Volume.size(); j++)
				{
					int n = 0;
					long m = Long.MAX_VALUE;
					for(int k = j; k < th.SPT[i].Volume.size(); k++)
					{
						if(m > th.SPT[i].Time.get(k))
						{
							n = k;
							m = th.SPT[i].Time.get(k);
						}
					}
					th.SPT[i].Volume.add(j, th.SPT[i].Volume.get(n));
					th.SPT[i].Volume.remove(n+1);
					th.SPT[i].Freque.add(j, th.SPT[i].Freque.get(n));
					th.SPT[i].Freque.remove(n+1);
					th.SPT[i].FrequI.add(j, th.SPT[i].FrequI.get(n));
					th.SPT[i].FrequI.remove(n+1);
					th.SPT[i].Time.add(j, th.SPT[i].Time.get(n));
					th.SPT[i].Time.remove(n+1);
					th.SPT[i].SoundT.add(j, th.SPT[i].SoundT.get(n));
					th.SPT[i].SoundT.remove(n+1);
					th.SPT[i].Duty.add(j, th.SPT[i].Duty.get(n));
					th.SPT[i].Duty.remove(n+1);
					th.SPT[i].Voldow.add(j, th.SPT[i].Voldow.get(n));
					th.SPT[i].Voldow.remove(n+1);
					th.SPT[i].Fredow.add(j, th.SPT[i].Fredow.get(n));
					th.SPT[i].Fredow.remove(n+1);
					th.SPT[i].VolDUM.add(j, th.SPT[i].VolDUM.get(n));
					th.SPT[i].VolDUM.remove(n+1);
				}
			}
			Time = System.currentTimeMillis();
			isSort = false;
		}
	}
}
