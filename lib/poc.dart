import 'package:deeplink/screen_a.dart';
import 'package:deeplink/screen_b.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'bloc.dart';

class PocWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    DeepLinkBloc _bloc = Provider.of<DeepLinkBloc>(context);
    return StreamBuilder<String>(
      stream: _bloc.state,
      builder: (context, snapshot) {
        print('inside snapshot');

        if (snapshot.hasData) {
          var path = Uri.parse(snapshot.data!).path;
          print('path');
          print(path);
          switch (path) {
            case '/screenA':
              print('A');
              WidgetsBinding.instance!.addPostFrameCallback((_) {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => ScreenA()),
                );
              });
              break; // Add this break statement
            case '/screenB':
              print('B');
              WidgetsBinding.instance!.addPostFrameCallback((_) {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => ScreenB()),
                );
              });
              break;
            // Add more cases as needed
          }
        }
        return Container(
            child: Center(
                child: Text(
          snapshot.hasData
              ? 'Redirected: ${snapshot.data}'
              : 'No deep link was used',
          style: Theme.of(context).textTheme.subtitle1,
        )));
      },
    );
  }
}
