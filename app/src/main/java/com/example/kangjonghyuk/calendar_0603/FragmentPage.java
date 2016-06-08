package com.example.kangjonghyuk.calendar_0603;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TableRow.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentPage extends Fragment implements DataConst{

    Activity m_activity;
    private int m_positionNumber;
    private Calendar m_cal;
    private GregorianCalendar m_gCal;
    private int m_year = 0;
    private int m_month = 0;

    private int m_startDayOfWeek = 0;
    private int m_maxDay = 0;
    private int m_oneday_width = 0;
    private int m_oneday_height = 0;

    ArrayList<String> m_daylist;
    ArrayList<String> m_actlist;

    TextView aDateTxt;

    private int m_dayCnt;
    private int m_select = -1;
    Oneday[] oneday;

    myDB my;
    SQLiteDatabase sql;

    public static FragmentPage newInstance(int pageNumber) {
        FragmentPage fragment = new FragmentPage();
        Bundle args = new Bundle();
        args.putInt("page", (pageNumber));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        my = new myDB(getContext());

        m_activity = getActivity();
        m_positionNumber = getArguments().getInt("page");

        m_cal = Calendar.getInstance();
        m_gCal = new GregorianCalendar();

        m_year = m_cal.get(Calendar.YEAR);
        m_month = m_positionNumber;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.calendarlayout, container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        aDateTxt = (TextView) getView().findViewById(R.id.CalendarMonthTxt);

        printDate(String.valueOf(m_year), String.valueOf(m_positionNumber + 1));

        m_cal.set(m_year, m_positionNumber, 1);
        m_gCal.set(m_year, m_positionNumber, 1);
        m_startDayOfWeek = m_cal.get(Calendar.DAY_OF_WEEK);

        m_maxDay = m_gCal.getActualMaximum((Calendar.DAY_OF_MONTH));
        if (m_daylist == null) m_daylist = new ArrayList<String>();
        m_daylist.clear();

        if (m_actlist == null) m_actlist = new ArrayList<String>();
        m_actlist.clear();

        m_daylist.add(SUNDAY);
        m_actlist.add("");
        m_daylist.add(MONTDAY);
        m_actlist.add("");
        m_daylist.add(THUESDAY);
        m_actlist.add("");
        m_daylist.add(WEDNESDAY);
        m_actlist.add("");
        m_daylist.add(THURSDAY);
        m_actlist.add("");
        m_daylist.add(FRIDAY);
        m_actlist.add("");
        m_daylist.add(SATURDAY);
        m_actlist.add("");

        if (m_startDayOfWeek != 1) {
            m_gCal.set(m_year, m_positionNumber - 1, 1);
            int prevMonthMaximumDay = (m_gCal.getActualMaximum((Calendar.DAY_OF_MONTH)) + 2);
            for (int i = m_startDayOfWeek; i > 1; i--) {
                m_daylist.add(Integer.toString(prevMonthMaximumDay - i));
                m_actlist.add(PREVIOUS_MONTH);
            }
        }

        for (int i = 1; i <= m_maxDay; i++) {
            m_daylist.add(Integer.toString(i));
            m_actlist.add("");
        }

        int dayDummy = (m_startDayOfWeek - 1) + m_maxDay;
        if (dayDummy > 35) {
            dayDummy = 42 - dayDummy;
        } else {
            dayDummy = 35 - dayDummy;
        }

        if (dayDummy != 0) {
            for (int i = 1; i <= dayDummy; i++) {
                m_daylist.add(Integer.toString(i));
                m_actlist.add(NEXT_MONTH);
            }
        }

        oneday = new Oneday[m_daylist.size()];
        final Calendar today = Calendar.getInstance();
        TableLayout tl = (TableLayout) getView().findViewById(R.id.tl_calendar_monthly);
        tl.removeAllViews();

        m_dayCnt = 0;
        int maxRow = ((m_daylist.size() > 42) ? 7 : 6);
        int maxColumn = 7;

        m_oneday_width = m_activity.getWindow().getWindowManager().getDefaultDisplay().getWidth();
        m_oneday_height = m_activity.getWindow().getWindowManager().getDefaultDisplay().getHeight();

        m_oneday_height = ((((m_oneday_height >= m_oneday_width) ? m_oneday_height : m_oneday_width) - tl.getTop()) / (maxRow + 1)) - 10;
        m_oneday_width = (m_oneday_width / maxColumn) + 1;

        int m_daylistsize = m_daylist.size() - 1;
        for (int i = 1; i <= maxRow; i++) {
            TableRow tr = new TableRow(getContext());
            tr.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            for (int j = 1; j <= maxColumn; j++) {
                oneday[m_dayCnt] = new Oneday(getContext());

                if ((m_dayCnt % 7) == 0) {
                    oneday[m_dayCnt].setTextDayColor(Color.RED);
                } else if ((m_dayCnt % 7) == 6) {
                    oneday[m_dayCnt].setTextDayColor(Color.BLUE);
                } else {
                    oneday[m_dayCnt].setTextDayColor(Color.BLACK);
                }

                if (m_dayCnt >= 0 && m_dayCnt < 7) {
                    oneday[m_dayCnt].setBgDayPaint(Color.DKGRAY);
                    oneday[m_dayCnt].setTextDayTopPadding(8);
                    oneday[m_dayCnt].setTextDayColor(Color.WHITE);
                    oneday[m_dayCnt].setTextDaySize(20);
                    oneday[m_dayCnt].setLayoutParams(new LayoutParams(m_oneday_width, 35));
                    oneday[m_dayCnt].isToday = false;
                } else {
                    oneday[m_dayCnt].isToday = false;
                    oneday[m_dayCnt].setDayOfWeek(m_dayCnt % 7 + 1);
                    oneday[m_dayCnt].setDay(Integer.valueOf(m_daylist.get(m_dayCnt)).intValue());
                    oneday[m_dayCnt].setTextActcntSize(30);
                    oneday[m_dayCnt].setTextActcntColor(Color.BLACK);
                    oneday[m_dayCnt].setTextActcntTopPadding(28);
                    oneday[m_dayCnt].setBgSelectedDayPaint(Color.rgb(0, 162, 232));
                    oneday[m_dayCnt].setBgTodayPaint(Color.LTGRAY);
                    oneday[m_dayCnt].setBgActcntPaint(Color.rgb(251, 247, 176));
                    oneday[m_dayCnt].setLayoutParams(new LayoutParams(m_oneday_width, m_oneday_height));

                    if (m_actlist.get(m_dayCnt).equals(PREVIOUS_MONTH)) {
                        oneday[m_dayCnt].setTextDaySize(18);
                        m_actlist.set(m_dayCnt, "");
                        oneday[m_dayCnt].setTextDayTopPadding(-4);

                        if (m_month - 1 < Calendar.JANUARY) {
                            oneday[m_dayCnt].setMonth(Calendar.DECEMBER);
                            oneday[m_dayCnt].setYear(m_year - 1);
                        } else {
                            oneday[m_dayCnt].setMonth(m_month - 1);
                            oneday[m_dayCnt].setYear(m_year);
                        }

                    } else if (m_actlist.get(m_dayCnt).equals(NEXT_MONTH)) {
                        oneday[m_dayCnt].setTextDaySize(18);
                        m_actlist.set(m_dayCnt, "");
                        oneday[m_dayCnt].setTextDayTopPadding(-4);
                        if (m_month + 1 > Calendar.DECEMBER) {
                            oneday[m_dayCnt].setMonth(Calendar.JANUARY);
                            oneday[m_dayCnt].setYear(m_year + 1);
                        } else {
                            oneday[m_dayCnt].setMonth(m_month + 1);
                            oneday[m_dayCnt].setYear(m_year);
                        }

                    } else {
                        oneday[m_dayCnt].setTextDaySize(50);
                        oneday[m_dayCnt].setYear(m_year);
                        oneday[m_dayCnt].setMonth(m_month);

                        String str = String.valueOf(m_year) + String.valueOf(m_month + 1) + oneday[m_dayCnt].getDay();
                        sql = my.getReadableDatabase();
                        Cursor cursor;
                        cursor = sql.rawQuery("SELECT * FROM MEMO WHERE date=" + str + ";", null);

                        String str1 = "";
                        while (cursor.moveToNext()) {
                            str1 += cursor.getString(1);
                        }

                        if (str1.length() != 0) {
                            m_actlist.set(m_dayCnt, "o");
                            oneday[m_dayCnt].setTextActcntColor(Color.MAGENTA);
                        }

                        if (oneday[m_dayCnt].getDay() == today.get(Calendar.DAY_OF_MONTH)
                                && oneday[m_dayCnt].getMonth() == today.get(Calendar.MONTH)
                                && oneday[m_dayCnt].getYear() == today.get(Calendar.YEAR)) {
                            oneday[m_dayCnt].isToday = true;
                            m_actlist.set(m_dayCnt, "오늘");

                            if (str1.length() != 0) {
                                NotificationManager nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, new Intent(getContext(), FragmentPage.class), PendingIntent.FLAG_UPDATE_CURRENT);

                                Notification.Builder mBuilder = new Notification.Builder(getContext());
                                mBuilder.setSmallIcon(R.drawable.galic);
                                mBuilder.setTicker("Notification 테스트입니다");
                                mBuilder.setWhen(System.currentTimeMillis());
                                mBuilder.setNumber(10);
                                mBuilder.setContentTitle("오늘 메모가 있습니다.");
                                mBuilder.setContentText("" + str1);
                                mBuilder.setContentIntent(pendingIntent);
                                mBuilder.setAutoCancel(true);
                                mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
                                nm.notify(111, mBuilder.build());
                                cursor.close();
                                sql.close();
                            }
                            oneday[m_dayCnt].invalidate();
                            m_select = m_dayCnt;
                        }
                    }

                    oneday[m_dayCnt].setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
//                          Toast.makeText(getContext(), iYear+"-"+(iMonth+1)+"-"+oneday[v.getId()].getTextDay(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getContext(), MemoPage.class);
                            intent.putExtra("YEAR", m_year);
                            intent.putExtra("MONTH", (m_month + 1));
                            intent.putExtra("DATE", oneday[v.getId()].getTextDay());
                            startActivityForResult(intent, 0);

                            return false;

                        }
                    });


                    oneday[m_dayCnt].setOnTouchListener(new OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            if (oneday[v.getId()].getTextDay() != "" && event.getAction() == MotionEvent.ACTION_UP) {
                                if (m_select != -1) {
                                    oneday[m_select].setSelected(false);
                                    oneday[m_select].invalidate();
                                }
                                oneday[v.getId()].setSelected(true);
                                oneday[v.getId()].invalidate();
                                m_select = v.getId();
                            }
                            return false;
                        }
                    });
                }

                oneday[m_dayCnt].setTextDay(m_daylist.get(m_dayCnt).toString());
                oneday[m_dayCnt].setTextActCnt(m_actlist.get(m_dayCnt).toString());
                oneday[m_dayCnt].setId(m_dayCnt);
                oneday[m_dayCnt].invalidate();
                tr.addView(oneday[m_dayCnt]);

                if (m_daylistsize != m_dayCnt) {
                    m_dayCnt++;
                } else {
                    break;
                }
            }
            tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }

    private void printDate(String thisYear, String thisMonth) {
        if (thisMonth.length() == 1) {
            aDateTxt.setText(String.valueOf(thisYear) + "." + "0" + thisMonth);
        } else {
            aDateTxt.setText(String.valueOf(thisYear) + "." + thisMonth);
        }
    }

}