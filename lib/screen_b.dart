import 'package:flutter/material.dart';

class ScreenB extends StatelessWidget {
  const ScreenB({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Container(
          width: 200,
          height: 400,
          color: Colors.redAccent,
        ),
      ),
    );
  }
}
