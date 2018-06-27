package suan.mydns.jp;

import java.awt.Component;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;

import processing.core.PApplet;
import suan.mydns.jp.channel.Channel;
import suan.mydns.jp.io.SaveLoad;
import suan.mydns.jp.io.WaveOut;
import suan.mydns.jp.music.MM2;
import suan.mydns.jp.music.MSTART;
import suan.mydns.jp.state.DeleteOverlap;
import suan.mydns.jp.state.KMState;
import suan.mydns.jp.state.Move;
import suan.mydns.jp.state.Sort;
import suan.mydns.jp.state.State;
import suan.mydns.jp.track.FourCh;
import suan.mydns.jp.track.OneCh;
import suan.mydns.jp.track.SuperTrack;
import suan.mydns.jp.track.ThreeCh;
import suan.mydns.jp.track.TwoCh;
import suan.mydns.jp.version.Version;

public class Thoone extends PApplet
{
	public MM2 mm2 = new MM2();
	public KMState kmState = new KMState(this);
	public Channel ch = new Channel();
	public SuperTrack[] SPT = new SuperTrack[4];
	public MSTART musics = new MSTART();
	public Sort sort = new Sort();
	public SaveLoad sl = new SaveLoad();
	public DeleteOverlap DO = new DeleteOverlap();
	public State state = new State();
	public Move mv = new Move();
	private boolean pressed = false;
	public static Version ver = new Version();

	public static final String Version = "1.4.1";

	DropTarget dropTarget;
	Component component;
	String path = "";
	boolean loads = false;
	boolean FileWrite = false;
	public static long FileWriteTime = 0;

	public static boolean newVersionAvairable = false;

	public static void main(String[] args)
	{
		// TODO 自動生成されたメソッド・スタブ
		PApplet.main("suan.mydns.jp.Thoone");
	}

	class myThread extends Thread
	{
		@Override
		public void run()
		{
			String str = ver.VirsionCheck();
			if(str != null)
			{
				int i = Integer.parseInt(Version.substring(0, Version.indexOf(".")));
				int j = Integer.parseInt(str.substring(0, str.indexOf(".")));
				if(i < j) newVersionAvairable = true;
				if(i == j)
				{
					int a = Version.indexOf(".");
					int b = str.indexOf(".");
					i = Integer.parseInt(Version.substring(a+1, Version.indexOf(".", a+1)));
					j = Integer.parseInt(str.substring(b+1, str.indexOf(".", b+1)));
					if(i < j) newVersionAvairable = true;
					if(i == j)
					{
						a = Version.indexOf(".", a+1);
						b = str.indexOf(".", b+1);
						i = Integer.parseInt(Version.substring(a+1));
						j = Integer.parseInt(str.substring(b+1));
						if(i < j) newVersionAvairable = true;
					}
				}
				System.out.println("Version Check Finished");
			}
			if(newVersionAvairable)
			{
				JFrame JF = new JFrame();
				JF.setSize(200, 150);
				JF.setLocationRelativeTo(null);
				JF.setAlwaysOnTop(true);

				JLabel JL = new JLabel();
				JL.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
				JL.setText("<html>New version available<br>Version " + str + "</html>");
				JL.setHorizontalAlignment(JLabel.CENTER);
				JL.setVerticalAlignment(JLabel.CENTER);

				JF.add(JL);

				JF.setVisible(true);
			}
		}
	}

	@Override
	public void settings()
	{
		myThread thre = new myThread();
		thre.start();
		size(1280, 720);
	}

	@Override
	public void setup()
	{
		this.getSurface().setTitle("Thoone  Ver " + Version);
		SPT[0] = new OneCh();
		SPT[1] = new TwoCh();
		SPT[2] = new ThreeCh();
		SPT[3] = new FourCh();

		component = (Component)this.surface.getNative();

		dropTarget = new DropTarget(component, new DropTargetListener() {
			public void dragEnter(DropTargetDragEvent dtde) {
			}
			public void dragOver(DropTargetDragEvent dtde) {
			}
			public void dropActionChanged(DropTargetDragEvent dtde) {
			}
			public void dragExit(DropTargetEvent dte) {
			}
			public void drop(DropTargetDropEvent dtde) {
				dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
				Transferable trans = dtde.getTransferable();
				List<File> fileNameList = null;
				if (trans.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					try {
						fileNameList = (List<File>)
								trans.getTransferData(DataFlavor.javaFileListFlavor);
					}
					catch (UnsupportedFlavorException ex) {
						println(ex);
					}
					catch (IOException ex) {
						println(ex);
					}
				}
				if (fileNameList == null) return;

				for (File f : fileNameList) {
					println(f.getAbsolutePath());
					path = f.getAbsolutePath();
				}
				loads = true;
			}
		}
				);
	}

	@Override
	public void draw()
	{
		fill(255);
		rect(0, 0, 1280, 720);
		SPT[ch.GetChannel()].Draw(this);
		sort.Draw(this);
		DO.Draw(this);
		state.Draw(this);
		sl.Draw(this);
		ch.Draw(this);
		mv.Draw(this);
		musics.Draw(this);
		musics.Play(this);
		kmState.Mouse[0] = this.mouseX;
		kmState.Mouse[1] = this.mouseY;
		if(!pressed && this.kmState.KeyC.get(' '))
		{
			pressed = true;
			this.musics.start = !this.musics.start;
			if(!this.musics.start)
			{
				for(int i = 0; i < 4; i++)
				{
					this.musics.nowNotes[i] = 0;
				}
				this.mm2.resk = true;
				this.mm2.resktime = System.currentTimeMillis();
			}
		}
		else if(pressed && !this.kmState.KeyC.get(' '))
		{
			for(int i = 0; i < 4; i++)
			{
				this.mm2.ChStat(i == 3 ? 0 : -1, 0.5f, 16, 0.0f, 1.0f, true, -1, i, 16);
			}
			pressed = false;
		}
		if(this.mm2.resk && System.currentTimeMillis() - this.mm2.resktime > 100) this.mm2.resk = false;

		if(this.loads)
		{
			this.loads = false;
			sl.Load(this, path);
		}

		if(kmState.Key.get(this.LEFT))
		{
			SPT[ch.GetChannel()].ShiftX += 20;
			if(SPT[ch.GetChannel()].ShiftX > 0) SPT[ch.GetChannel()].ShiftX = 0;
		}
		if(kmState.Key.get(this.RIGHT))
		{
			SPT[ch.GetChannel()].ShiftX -= 20;
		}
		if(kmState.Key.get(this.UP))
		{
			SPT[ch.GetChannel()].ShiftY += 20;
			if(SPT[ch.GetChannel()].ShiftY > 2580) SPT[ch.GetChannel()].ShiftY = 2580;
		}
		if(kmState.Key.get(this.DOWN))
		{
			SPT[ch.GetChannel()].ShiftY -= 20;
			if(SPT[ch.GetChannel()].ShiftY < 720) SPT[ch.GetChannel()].ShiftY = 720;
		}

		if(kmState.KeyC.get('r') && !FileWrite)
		{
			FileWrite = true;
			WaveOut wo = new WaveOut();
			wo.start();
		}
		else if(!kmState.KeyC.get('r') && FileWrite)
		{
			FileWrite = false;
		}
		if(System.currentTimeMillis() - this.FileWriteTime < 1000)
		{
			this.textSize(50);
			this.fill(0);
			this.text("Wave Output Finish", 400, 400);
		}
	}

	@Override
	public void mousePressed()
	{
		if( mouseButton == LEFT )
		{
			//左ボタンが押されたときの処理
			kmState.MLeft = true;
		}
		else if( mouseButton == CENTER )
		{
			//中央ボタンが押されたときの処理
			kmState.MCenter = true;
		}
		else if( mouseButton == RIGHT )
		{
			//右ボタンが押されたときの処理
			kmState.MRight = true;
		}
	}

	@Override
	public void mouseReleased()
	{
		if( mouseButton == LEFT )
		{
			//左ボタンが押されたときの処理
			kmState.MLeft = false;
		}
		else if( mouseButton == CENTER )
		{
			//中央ボタンが押されたときの処理
			kmState.MCenter = false;
		}
		else if( mouseButton == RIGHT )
		{
			//右ボタンが押されたときの処理
			kmState.MRight = false;
		}
	}


	@Override
	public void keyPressed()
	{
		kmState.Key.put(keyCode, true);
		kmState.KeyC.put(key, true);
		//System.out.println(this.key + " " + this.keyCode);
	}

	@Override
	public void keyReleased()
	{
		kmState.Key.put(keyCode, false);
		kmState.KeyC.put(key, false);
	}

	void keyPressed(java.awt.event.KeyEvent e)
	{
		int mod = e.getModifiersEx();
		if (e.getKeyCode()==java.awt.event.KeyEvent.VK_Z&&(mod & java.awt.event.InputEvent.CTRL_DOWN_MASK) != 0) {
			System.out.println("ctrl+z");
		}
	}
}
