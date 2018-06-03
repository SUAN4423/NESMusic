package suan.mydns.jp.track;

import suan.mydns.jp.Thoone;

public class ThreeCh extends SuperTrack
{
	@Override
	public void Draw(Thoone th)
	{
		super.Draw(th);

		this.Onkai(th, 2);

		if(th.mv.enableMove) th.mv.move(th, this);

		this.Shift(th);
	}
}
