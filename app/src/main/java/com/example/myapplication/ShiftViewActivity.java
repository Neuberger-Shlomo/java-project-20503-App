

    package com.example.myapplication;

    import android.graphics.Color;
    import android.graphics.PorterDuff;
    import android.os.Bundle;
    import android.view.Gravity;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.TableLayout;
    import android.widget.TableRow;
    import android.widget.TextView;
    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.core.content.ContextCompat;
    import androidx.fragment.app.Fragment;
    import com.example.myapplication.Model.Shift;
    import java.time.DayOfWeek;
    import java.time.LocalDate;
    import java.time.format.DateTimeFormatter;
    import java.util.ArrayList;
    import java.util.List;

    public class ShiftViewActivity extends Fragment {
        private TableLayout shiftsTable;
        private Button sendButton;
        private List<Shift> Availableshifts = new ArrayList<>();
        private List<Shift> selectedShifts = new ArrayList<>();
        private ArrayList<View> views= new ArrayList<View>();

        private int maxShiftsPerDay = 4; // MAXIMUM shifts per day in the week (to know the number of table rows)

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }
       // CREATING DUMMY LIST OF SHIFTS
        List<Shift> createListOfShifts(List<Shift> availableshifts) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (int i = 0; i < 7 * maxShiftsPerDay; i++) {
                int dayIndex = i % 7;
                //example week from  2023-04-22
                String dateString = "2023-04-" + (22 + dayIndex);
                LocalDate date = LocalDate.parse(dateString, formatter);
                //CONVERT DAY OF WEEK TO NUMBER
                DayOfWeek dayOfWeek = date.getDayOfWeek();
                int dayOfWeekValue = dayOfWeek.getValue() % 7;

                int startHour = 2 + i;
                int duration = 2 ;

                int endHour = (startHour + duration) % 24;
                Availableshifts.add(new Shift(dateString, 2, new ArrayList<>(), i + 2, startHour, duration));

            }
            Availableshifts.add(new Shift("2023-04-22", 2, new ArrayList<>(), 99 + 2, 2, 2));

            System.out.println(Availableshifts);

            return Availableshifts;
        }
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.activity_shift_view, container, false);
            shiftsTable = view.findViewById(R.id.shift_table);
            sendButton = view.findViewById(R.id.send_button);
            sendButton.setOnClickListener(sendButtonClickListener);
            createListOfShifts(Availableshifts);
            InsertTableData(Availableshifts);
            return view;
        }

        private void InsertTableData(List<Shift> shifts) {
            //SET TABLE AND FILL TABLE HEADLINE
            TableRow headerRow = new TableRow(requireContext());
            String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

            for (String day : daysOfWeek) {
                TextView headerTextView = new TextView(requireContext());
                headerTextView.setText(day);
                headerTextView.setGravity(Gravity.CENTER);
                headerTextView.setBackgroundColor(Color.GRAY);
                headerTextView.setPadding(10, 8, 10, 8);
                headerRow.addView(headerTextView);
            }

            shiftsTable.addView(headerRow);
            // ADD BUTTONS AND SET BUTTONS
            for (int i = 0; i < maxShiftsPerDay; i++) {
                TableRow row = new TableRow(requireContext());

                for (int j = 0; j < 7; j++) {
                    Button shiftButton = new Button(requireContext());
                    int shiftIndex = i * 7 + j;
                    Shift shift = shifts.get(shiftIndex);
                    shiftButton.setText(shift.getStartHour() + ":00 - " + ((shift.getStartHour() + shift.getDuration()) % 24) + ":00");
                    shiftButton.setId(shiftIndex);

                    shiftButton.setPadding(4, 4, 4, 4);
                    shiftButton.setOnClickListener(shiftButtonClickListener);
                    row.addView(shiftButton);



                }
                shiftsTable.addView(row);
            }
        }
        private View.OnClickListener shiftButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Button button = (Button) view;
                // SET THE  SHIFT IF CHOOSED\ NOT CHOOSED
                if (button.getTag() == null || !((boolean) button.getTag())) {
                    button.getBackground().setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple_200), PorterDuff.Mode.MULTIPLY);
                    button.setTag(true);
                    views.add(view);
                } else {
                    button.getBackground().clearColorFilter();
                    button.setTag(false);
                    views.remove(view);
                }
            }
        };
        //send button listener

            private View.OnClickListener sendButtonClickListener = new View.OnClickListener() {

                public void onClick(View view) {
                System.out.println("Send button clicked");
              //  addSelectedShifts();
                    for(View v:views){
                        Button button = (Button) v;
                        selectedShifts.add(Availableshifts.get(button.getId()));

                    }
                // TO DO later : Send the selectedShifts list to your server
                System.out.println("Selected shifts: " + selectedShifts);

            }
        };

        // UPDTE LIST OF SHIFTS
        private void addSelectedShifts() {
            selectedShifts.clear();
            for (int i = 0; i <= maxShiftsPerDay; i++) {
                TableRow row = (TableRow) shiftsTable.getChildAt(i);
                for (int j = 0; j < 7; j++) {
                    if (row.getChildAt(j) instanceof Button) {
                        Button button = (Button) row.getChildAt(j);
                        if (button.getTag() != null && (boolean) button.getTag()) {
                            int shiftIndex = (i) * maxShiftsPerDay + j; // subtract 1 because the first row is the header
                            selectedShifts.add(Availableshifts.get(shiftIndex));
                        }
                    }
                }
            }
        }
    }
