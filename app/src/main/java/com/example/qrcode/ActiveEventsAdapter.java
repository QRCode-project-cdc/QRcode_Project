package com.example.qrcode;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActiveEventsAdapter extends RecyclerView.Adapter<ActiveEventsAdapter.EventViewHolder> {

    private List<Event> eventList;
    private Context context;
    private DatabaseHelper databaseHelper;
    private RegisteredEventAdapter.OnEventClickListener listener;

    public ActiveEventsAdapter(Context context, List<Event> eventList, RegisteredEventAdapter.OnEventClickListener listener) {
        this.context = context;
        this.eventList = eventList;
        this.databaseHelper = DatabaseHelper.getInstance(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_active_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.textViewEventTitle.setText(event.getTitle());
        holder.textViewEventDescription.setText(event.getDescription());

        // Atualiza a lista de alunos escaneados
        updateScannedStudentsList(holder, event.getId());

        // Configurar o botão de alunos
        holder.buttonStudents.setOnClickListener(v -> {
            Log.d("ActiveEventsAdapter", "Botão de alunos clicado para o evento: " + event.getTitle());

            // Recupera a lista de alunos do evento
            List<Student> studentsList = databaseHelper.getStudentsForEvent(event.getId());

            // Cria uma lista de nomes dos alunos (ou qualquer outra informação que você queira passar)
            List<String> studentNamesList = new ArrayList<>();
            for (Student student : studentsList) {
                studentNamesList.add(student.getName());  // Assuming you have a getName() method in Student class
            }

            // Cria um Intent para abrir a StudentListActivity
            Intent intent = new Intent(context, StudentListActivity.class);
            // Passa a lista de nomes de alunos para a nova Activity
            intent.putStringArrayListExtra("studentsList", new ArrayList<>(studentNamesList));
            context.startActivity(intent);
        });




        holder.buttonDelete.setOnClickListener(v -> {
            // Chama a função de exclusão
            deleteEvent(position, event.getId());
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size ();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEventTitle;
        TextView textViewEventDescription;
        TextView textViewScannedStudents; // Adicionando TextView para alunos escaneados
        Button buttonStudents;
        Button buttonDelete;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEventTitle = itemView.findViewById(R.id.textViewEventTitle);
            textViewEventDescription = itemView.findViewById(R.id.textViewEventDescription);
            textViewScannedStudents = itemView.findViewById(R.id.textViewScannedStudents); // Referência para a TextView
            buttonStudents = itemView.findViewById(R.id.buttonStudents);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);

            if (buttonStudents == null) {
                Log.e("EventViewHolder", "buttonStudents é null");
            }
        }
    }

    private void updateScannedStudentsList(EventViewHolder holder, int eventId) {
        List<String> scannedStudents = databaseHelper.getScannedStudents(eventId);
        StringBuilder studentsList = new StringBuilder();
        for (String student : scannedStudents) {
            studentsList.append(student).append("\n");
        }
        holder.textViewScannedStudents.setText(studentsList.toString());
    }

    public void registerStudentAttendance(String studentRgm, int eventId) {
        String timestamp = getCurrentTime();
        if (databaseHelper.registerAttendance(studentRgm, eventId, timestamp)) {
            Toast.makeText(context, "Presença registrada para o aluno: " + studentRgm, Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        } else {
            Toast.makeText(context, "Erro ao registrar presença.", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void deleteEvent(int position, int eventId) {
        // Remove do banco de dados
        boolean deleted = databaseHelper.deleteEvent(eventId);
        if (deleted) {
            // Remove da lista e notifica o adapter
            eventList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Evento excluído com sucesso.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Erro ao excluir o evento.", Toast.LENGTH_SHORT).show();
        }
    }
}