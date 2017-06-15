package bensach.saul.terreta;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by saul on 10/06/17.
 */

public class MemberAdapter extends ArrayAdapter<Member> {

    public MemberAdapter(@NonNull Context context,  @NonNull List<Member> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Member member = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.member_adapter, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.text_name_adapter);
        name.setText(member.getName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), DisplayMember.class);
                i.putExtra("member", member);
                getContext().startActivity(i);
            }
        });
        return convertView;
    }
}
